package com.example.weatherpulse.data.source.local

import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.data.source.IDataSource
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private var weatherDAO: WeatherDAO) : IDataSource {
    companion object {
        private var instance: WeatherLocalDataSource? = null

        fun getInstance(weatherDAO: WeatherDAO): WeatherLocalDataSource {
            return instance ?: synchronized(this) {
                val temp = WeatherLocalDataSource(weatherDAO)
                instance = temp
                temp
            }
        }
    }

    override suspend fun getCurrentWeather(): Flow<WeatherDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getWeatherForecast(): Flow<List<WeatherDTO>> {
        return weatherDAO.getWeatherTimestamps()
    }

}