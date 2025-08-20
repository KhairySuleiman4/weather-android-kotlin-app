package com.example.weatherapp.model.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofitInstance: Retrofit = Retrofit
        .Builder()
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService: APIServices = retrofitInstance.create(APIServices::class.java)
}

private fun provideOkHttpClient(): OkHttpClient {
    return OkHttpClient()
        .newBuilder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
        .addInterceptor(AuthInterceptor())
        .build()
}