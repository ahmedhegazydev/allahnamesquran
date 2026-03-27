package com.example.allahnamesquran.data.remote

import com.example.allahnamesquran.data.remote.dto.QuranPageResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuranApiService {

    @GET("api/quranPagesText")
    suspend fun getQuranPage(
        @Query("page") page: Int
    ): QuranPageResponseDto
}