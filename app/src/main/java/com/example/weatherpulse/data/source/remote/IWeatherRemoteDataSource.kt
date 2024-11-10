package com.example.weatherpulse.data.source.remote

import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(): Flow<WeatherDTO>
    suspend fun getForecastWeather(): Flow<List<WeatherDTO>>
}