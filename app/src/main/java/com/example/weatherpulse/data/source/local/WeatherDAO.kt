package com.example.weatherpulse.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherpulse.data.Weather
import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM weather_table WHERE isCurrentWeather = 1")
    fun getCurrentWeather(): Flow<WeatherDTO>

    @Query("SELECT * FROM weather_table WHERE isCurrentWeather = 0")
    fun getForecastWeather() : Flow<List<WeatherDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrentWeather(currentWeather: WeatherDTO): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecastWeather(forecastWeather: List<WeatherDTO>): List<Long>

    @Query("DELETE FROM weather_table WHERE isCurrentWeather = 1")
    suspend fun deleteCurrentWeatherData()

    @Query("DELETE FROM weather_table WHERE isCurrentWeather = 0")
    suspend fun deleteForecastWeatherData()

}