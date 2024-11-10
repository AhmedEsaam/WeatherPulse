package com.example.weatherpulse.data.source

import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getCurrentWeather(): Flow<WeatherDTO>
    suspend fun getForecastWeather(): Flow<List<WeatherDTO>>

    suspend fun insertCurrentWeather(currentWeather: WeatherDTO)
    suspend fun insertForecastWeather(forecastWeather: List<WeatherDTO>)
}