package com.example.weatherpulse.data.source.remote

import com.example.weatherpulse.data.ForecastResponse
import com.example.weatherpulse.data.WeatherDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherJSON(
        @Query("q") city: String = "cairo",
        @Query("appid") apiKey: String = "c178d2436cbe6932631665eab01d2bc2",
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en"
    ): Response<WeatherDTO>


    @GET("forecast")
    suspend fun getForecastJSON(
        @Query("q") city: String = "cairo",
        @Query("appid") apiKey: String = "c178d2436cbe6932631665eab01d2bc2",
        @Query("units") units: String = "metric",
        @Query("lang") language: String = "en"
    ): Response<ForecastResponse>

}

