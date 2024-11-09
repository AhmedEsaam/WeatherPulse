package com.example.weatherpulse.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherpulse.data.WeatherDTO

@Database(entities = arrayOf(WeatherDTO::class), version = 1)
@TypeConverters(WeatherTypeConverter::class)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDAO

    companion object {
        @Volatile
        private var INSTANCE : WeatherDatabase? = null;

        fun getInstance (ctx: Context) : WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val temp = Room.databaseBuilder(
                    ctx.applicationContext, WeatherDatabase::class.java, "weather_database"
                ).build()
                INSTANCE = temp
                temp
            }
        }

    }
}