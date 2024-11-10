package com.example.weatherpulse.data.source.local

import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun getCurrentWeather(): Flow<WeatherDTO>
    suspend fun getForecastWeather(): Flow<List<WeatherDTO>>
    suspend fun insertCurrentWeather(currentWeather: WeatherDTO): Long
    suspend fun insertForecastWeather(forecastWeather: List<WeatherDTO>): List<Long>

    suspend fun deleteCurrentWeatherData()
    suspend fun deleteForecastWeatherData()
}