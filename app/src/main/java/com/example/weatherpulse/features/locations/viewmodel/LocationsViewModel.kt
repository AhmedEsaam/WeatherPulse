package com.example.weatherpulse.features.locations.viewmodel

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

//    val currentWeatherResult: MutableStateFlow<Result<WeatherDTO>> = MutableStateFlow(Result.Loading)
//    val forecastResult: MutableStateFlow<Result<List<WeatherDTO>>> = MutableStateFlow(Result.Loading)

    init {
//        getCurrentWeather()
//        getForecastWeather()
    }

//    fun getCurrentWeather() = viewModelScope.launch(Dispatchers.IO) {
//        weatherRepository.getCurrentWeather()
//            .catch { e ->
//                currentWeatherResult.value = Result.Failure(e)
//            }
//            .collect { data ->
//                currentWeatherResult.value = Result.Success(data)
//            }
//    }
//
//    fun getForecastWeather() = viewModelScope.launch(Dispatchers.IO) {
//        weatherRepository.getForecastWeather()
//            .catch { e ->
//                forecastResult.value = Result.Failure(e)
//            }
//            .collect { data ->
//                forecastResult.value = Result.Success(data)
//            }
//    }
//
//    fun updateWeatherData() {
//        getCurrentWeather()
//        getForecastWeather()
//    }

}
