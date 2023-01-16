package com.griesbeck.travelio.models.weather

import android.util.Log
import androidx.lifecycle.MutableLiveData
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

object Constants {

    const val BASE_URL = "https://api.open-meteo.com/"
    const val DAILY = "weathercode,temperature_2m_max,temperature_2m_min"
    const val TIMEZONE = "Europe%2FBerlin"

}

class WeatherApi {


    fun getWeatherData(
        latitude: Double,
        longitude: Double,
        daily: String,
        timezone: String,
        liveData: MutableLiveData<Daily>
    ) {

        // Generates Java Interface to Weather API and converts response with GSON
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create instance of WeatherService
        val weatherService: WeatherService = retrofit.create(WeatherService::class.java)

        // Instantiate get api call for weather data
        val apiCall = weatherService.getWeatherData(latitude,longitude,daily,timezone)



       // Send api call to weather server and get response
        apiCall.enqueue(object: Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if(response.isSuccessful){
                    Log.i("info",response.body().toString())
                    liveData.postValue(response.body()?.daily)
                }else{
                    Log.i("info",response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

}

