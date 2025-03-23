package com.example.weatherapp.model.pojos.response

sealed class Response {
    data object Loading: Response()
    data class Success(val data: Any): Response()
    data class Failure(val error: Throwable): Response()
    data class Default(val default: Any): Response()
}