package com.example.weatherapp.model.remote

import com.example.weatherapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.WEATHER_API_KEY

        val newUrl = chain
            .request()
            .url
            .newBuilder()
            .addQueryParameter("appid", apiKey)
            .build()

        val request = chain
            .request()
            .newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }
}