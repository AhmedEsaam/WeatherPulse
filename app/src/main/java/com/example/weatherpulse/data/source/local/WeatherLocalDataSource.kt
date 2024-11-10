package com.example.weatherpulse.data.source.local

import android.util.Log
import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private var weatherDAO: WeatherDAO) : IWeatherLocalDataSource {
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

        return weatherDAO.getCurrentWeather()

    }

    override suspend fun getForecastWeather(): Flow<List<WeatherDTO>> {
        return weatherDAO.getForecastWeather()
    }

    override suspend fun insertCurrentWeather(currentWeather: WeatherDTO): Long {
        currentWeather.isCurrentWeather = true
        return weatherDAO.insertCurrentWeather(currentWeather)
    }

    override suspend fun insertForecastWeather(forecastWeather: List<WeatherDTO>): List<Long> {
        return weatherDAO.insertForecastWeather(forecastWeather)
    }

    override suspend fun deleteCurrentWeatherData() {
        weatherDAO.deleteCurrentWeatherData()
    }

    override suspend fun deleteForecastWeatherData() {
        weatherDAO.deleteForecastWeatherData()
    }

}