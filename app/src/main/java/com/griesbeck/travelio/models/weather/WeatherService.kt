package com.griesbeck.travelio.models.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("v1/forecast")
    fun getWeatherData(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("daily", encoded = true) daily: String,
        @Query("timezone", encoded = true) timezone: String
    ): Call<WeatherResponse>
}