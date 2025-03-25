package com.example.weatherapp.model.pojos.response

sealed class Response {
    data object Loading: Response()
    data class Success<out T>(val data: T): Response() // T or Any ?!
    data class Failure(val error: Throwable): Response()
    data class Default(val default: Any): Response()
}