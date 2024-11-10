package com.example.weatherpulse.data.source.remote

import com.example.weatherpulse.data.source.IDataSource
import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSource(private var weatherService: WeatherService) : IWeatherRemoteDataSource {

    companion object {
        private var instance: WeatherRemoteDataSource? = null

        fun getInstance(weatherService: WeatherService): WeatherRemoteDataSource {
            return instance ?: synchronized(this) {
                val temp = WeatherRemoteDataSource(weatherService)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getCurrentWeather(): Flow<WeatherDTO> {
        return flowOf(
            weatherService.getWeatherJSON(
                city = "cairo",
                units = "metric",
                language = "en"
            ).body() ?: WeatherDTO()
        )
    }

    override suspend fun getForecastWeather(): Flow<List<WeatherDTO>> {
        return flowOf(
            weatherService.getForecastJSON(
                city = "cairo",
                units = "metric",
                language = "en"
            ).body()?.list ?: listOf()
        )
    }
}