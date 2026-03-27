package com.example.allahnamesquran.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object NetworkModule {

    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    fun provideRetrofit(): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://quran.yousefheiba.com/")
            .addConverterFactory(provideJson().asConverterFactory(contentType))
            .build()
    }

    fun provideQuranApiService(): QuranApiService {
        return provideRetrofit().create(QuranApiService::class.java)
    }
}