package com.example.weatherpulse.data.source

import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface IDataSource {

    suspend fun getCurrentWeather() : Flow<WeatherDTO>
    suspend fun getWeatherForecast() : Flow<List<WeatherDTO>>
}