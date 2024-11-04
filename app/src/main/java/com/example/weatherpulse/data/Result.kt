package com.example.weatherpulse.data

import com.example.weatherpulse.data.Result.Failure
import com.example.weatherpulse.data.Result.Loading
import com.example.weatherpulse.data.Result.Success

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()

}


/**
 * string values of the different states of [Result]
 */
val Result<*>.stringValue
    get() = when (this) {
        is Success<*> -> "Success [data=$data]"
        is Failure -> "Failure [exception=$exception]"
        Loading -> "Loading..."
    }


/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null
