package com.example.weatherpulse.data.source.local

import androidx.room.TypeConverter
import com.example.weatherpulse.data.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherTypeConverter {
    @TypeConverter
    fun fromWeatherList(value: List<Weather>?): String? = Gson().toJson(value)

    @TypeConverter
    fun toWeatherList(value: String?): List<Weather>? {
        val listType = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, listType)
    }
}