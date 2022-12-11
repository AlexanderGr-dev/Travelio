package com.griesbeck.travelio.models.weather

import androidx.lifecycle.MutableLiveData

class WeatherRepository(private val weather: WeatherApi) {

    fun getWeatherData(latitude: Double,
                       longitude: Double,
                       daily: String,
                       timezone: String,
                       liveData: MutableLiveData<Daily>

    ) {
        return weather.getWeatherData(latitude,longitude,daily,timezone, liveData)
    }
}