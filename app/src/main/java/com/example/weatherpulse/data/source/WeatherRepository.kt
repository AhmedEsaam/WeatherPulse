package com.example.weatherpulse.data.source

import android.util.Log
import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import com.example.weatherpulse.data.Result
import com.example.weatherpulse.data.source.local.IWeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.IWeatherRemoteDataSource
import com.google.android.material.snackbar.Snackbar

class WeatherRepository(
    private var weatherRemoteDataSource: IWeatherRemoteDataSource,
    private var weatherLocalDataSource: IWeatherLocalDataSource
) : IWeatherRepository {

    companion object {
        @Volatile
        private var INSTANCE: WeatherRepository? = null

        fun getRepository(
            weatherRemoteDataSource: WeatherRemoteDataSource,
            weatherLocalDataSource: WeatherLocalDataSource
        ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val temp = WeatherRepository(
                    weatherRemoteDataSource,
                    weatherLocalDataSource
                )
                INSTANCE = temp
                temp
            }
        }
    }


    override suspend fun getCurrentWeather(): Flow<WeatherDTO> {
        try {
            val currentWeatherFlow: Flow<WeatherDTO> = weatherRemoteDataSource.getCurrentWeather()

            var remoteCurrentWeather: WeatherDTO? = null
            currentWeatherFlow.collectLatest { remoteCurrentWeather = it }
            remoteCurrentWeather?.let {
                weatherLocalDataSource.deleteCurrentWeatherData()
                weatherLocalDataSource.insertCurrentWeather(it)
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Network error", e)
        }

        return weatherLocalDataSource.getCurrentWeather()
    }


    override suspend fun getForecastWeather(): Flow<List<WeatherDTO>> {
        try {
            val forecastWeatherFlow: Flow<List<WeatherDTO>> = weatherRemoteDataSource.getForecastWeather()

            var remoteForecastWeather: List<WeatherDTO>? = null
            forecastWeatherFlow.collectLatest { remoteForecastWeather = it }
            remoteForecastWeather?.let {
                weatherLocalDataSource.deleteForecastWeatherData()
                weatherLocalDataSource.insertForecastWeather(it)
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Network error", e)
        }

        return weatherLocalDataSource.getForecastWeather()
    }


    override suspend fun insertCurrentWeather(currentWeather: WeatherDTO) {
        weatherLocalDataSource.insertCurrentWeather(currentWeather)
    }

    override suspend fun insertForecastWeather(forecastWeather: List<WeatherDTO>) {
        weatherLocalDataSource.insertForecastWeather(forecastWeather)
    }


}