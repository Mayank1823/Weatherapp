package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//e69ef9da3e8af5fb451d6f2440c4c956

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("Delhi")
        SearchCity()
    }

    private fun SearchCity() {
       val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

    })


    }

    private fun fetchWeatherData(cityname:String) {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build().create(ApiInterface::class.java)
            val response = retrofit.getWeatherData(cityname,"e69ef9da3e8af5fb451d6f2440c4c956","metric")
            response.enqueue(object:Callback<WeatherApp>{
                override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                    val responseBody = response.body()
                    if(response.isSuccessful && responseBody!=null){
                        val temperature = responseBody.main.temp.toString()
                        val humidity = responseBody.main.humidity
                        val windSpeed =responseBody.wind.speed
                        val sunRise =responseBody.sys.sunrise.toLong()
                        val sunSet= responseBody.sys.sunset.toLong()
                        val seaLevel = responseBody.main.pressure
                        val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
                        val maxTemp = responseBody.main.temp_max
                        val minTemp = responseBody.main.temp_min


                        binding.temp.text = "$temperature ℃"         // So we have used bindings by enabling it in module section and now we get id's of all the textviews directly..
                        binding.weather.text = condition
                        binding.maxTemp.text="Max Temp: $maxTemp ℃"
                        binding.minTemp.text="Min Temp: $minTemp ℃"
                        binding.humidity.text ="$humidity %"
                        binding.windspeed.text="$windSpeed m/s"
                        binding.sunrise.text="${time(sunRise)}"
                        binding.sunset.text="${time(sunSet)}"
                        binding.sea.text="$seaLevel hpa"
                        binding.conditions.text = condition
                        binding.day.text=dayName(System.currentTimeMillis())
                            binding.date.text =date()
                            binding.cityname.text="$cityname"









                       // Log.d("TAG", "onResponse: $temperature")

                        changeImages(condition)
                    }
                }

                override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

            }

    private fun changeImages(conditions:String) {
        when(conditions) {

            "Clear Sky", "Sunny" , "Clear"-> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
                "Partly Clouds", "Clouds" , "Overcast" , "Mist", "Foggy"-> {
                    binding.root.setBackgroundResource(R.drawable.colud_background)
                    binding.lottieAnimationView.setAnimation(R.raw.cloud)
                }

                "Light Rain", "Drizzle" , "Moderate Rain","Showers", "Heavy Rain"-> {
                        binding.root.setBackgroundResource(R.drawable.rain_background)
                        binding.lottieAnimationView.setAnimation(R.raw.rain)
                    }
                "Light Snow", "Moderate Snow" , "Heavy snow", "Blizzard"-> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)


            }
            else ->
            {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format(Date())

    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp*1000))

    }

    // Function is created to format the dayname correctly
    fun dayName(timestamp: Long):String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format(Date())

    }

}