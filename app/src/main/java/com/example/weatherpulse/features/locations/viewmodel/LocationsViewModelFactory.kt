package com.example.weatherpulse.features.locations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherpulse.data.source.WeatherRepository

class LocationsViewModelFactory(private val _repo: WeatherRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationsViewModel::class.java)) {
            LocationsViewModel(_repo) as T
        } else {
            throw IllegalArgumentException("ViewModel Class not found")
        }
    }
}