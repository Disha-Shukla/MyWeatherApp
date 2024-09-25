package com.example.myweatherapp

import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String

    ): WeatherResponse
}

class WeatherRepository @Inject constructor(private val weatherApi: WeatherApi) {
    suspend fun getWeather(city: String, apiKey: String): WeatherResponse {
        return weatherApi.getWeather(city, apiKey, "metric")
    }
}
