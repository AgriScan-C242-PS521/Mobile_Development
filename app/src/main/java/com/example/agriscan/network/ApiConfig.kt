package com.example.agriscan.network

import com.example.agriscan.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private fun createRetrofit(baseUrl: String): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun getTomatoApiService(): ApiService {
        return createRetrofit(BuildConfig.TOMATO_BASE_URL).create(ApiService::class.java)
    }

    fun getCornApiService(): ApiService {
        return createRetrofit(BuildConfig.CORN_BASE_URL).create(ApiService::class.java)
    }

    fun getPotatoApiService(): ApiService {
        return createRetrofit(BuildConfig.POTATO_BASE_URL).create(ApiService::class.java)
    }
}