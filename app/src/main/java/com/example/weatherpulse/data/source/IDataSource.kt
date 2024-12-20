package com.example.weatherpulse.data.source

import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface IDataSource {

    suspend fun getCurrentWeather(): Flow<WeatherDTO>
    suspend fun getForecastWeather(): Flow<List<WeatherDTO>>
    suspend fun insertCurrentWeather(currentWeather: WeatherDTO): Long
    suspend fun insertForecastWeather(forecastWeather: List<WeatherDTO>): List<Long>
    suspend fun deleteAllWeatherData()
}