package com.example.weatherpulse.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.example.weatherpulse.data.WeatherDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM weather_table")
    fun getWeatherTimestamps() : Flow<List<WeatherDTO>>

}