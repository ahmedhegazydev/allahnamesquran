package app.asmaquran.mobile.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://quran.yousefheiba.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideQuranApiService(): QuranApiService {
        return provideRetrofit().create(QuranApiService::class.java)
    }
}
