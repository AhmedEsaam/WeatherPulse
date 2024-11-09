package com.example.weatherpulse.data.source

import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.data.source.local.WeatherLocalDataSource
import com.example.weatherpulse.data.source.remote.WeatherRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class WeatherRepository(
    private var weatherRemoteDataSource: IDataSource,
    private var weatherLocalDataSource: IDataSource
) : IWeatherRepository {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

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

        return weatherRemoteDataSource.getCurrentWeather()
    }

    override suspend fun getWeatherForecast(): Flow<List<WeatherDTO>> {
        return weatherRemoteDataSource.getWeatherForecast()
    }
}