package com.griesbeck.travelio.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griesbeck.travelio.models.weather.Constants
import com.griesbeck.travelio.models.weather.Daily
import com.griesbeck.travelio.models.weather.WeatherApi
import com.griesbeck.travelio.models.weather.WeatherRepository

class WeatherViewModel: ViewModel() {

    private val repo = WeatherRepository(WeatherApi())
    private val _weather = MutableLiveData<Daily>()
    val weather: LiveData<Daily> get() = _weather

    fun getWeather(lat: Double, lon: Double){
        repo.getWeatherData(lat,lon,Constants.DAILY,Constants.TIMEZONE, _weather)
    }
}