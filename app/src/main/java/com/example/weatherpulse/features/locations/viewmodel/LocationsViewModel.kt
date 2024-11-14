package com.example.weatherpulse.features.locations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherpulse.data.Result
import com.example.weatherpulse.data.WeatherDTO
import com.example.weatherpulse.data.source.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class LocationsViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _location = MutableLiveData<Pair<Double, Double>>()

    init {
        _location.value = Pair(31.2355, 30.0441)
    }

    val location: LiveData<Pair<Double, Double>>
        get() = _location

    fun setLocation(lng: Double, lat: Double) {
        _location.value = Pair(lng, lat)
    }
}
