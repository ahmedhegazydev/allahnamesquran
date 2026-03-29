package app.asmaquran.mobile.data.remote

import app.asmaquran.mobile.data.remote.dto.QuranPageResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface QuranApiService {

    @GET("api/quranPagesText")
    suspend fun getQuranPage(
        @Query("page") page: Int
    ): QuranPageResponseDto
}