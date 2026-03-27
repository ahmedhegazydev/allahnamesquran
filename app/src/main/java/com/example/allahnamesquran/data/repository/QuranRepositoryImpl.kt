package com.example.allahnamesquran.data.repository

import com.example.allahnamesquran.data.local.dao.AyahDao
import com.example.allahnamesquran.data.mapper.AyahMapper
import com.example.allahnamesquran.data.preferences.AppPreferences
import com.example.allahnamesquran.data.remote.QuranApiService
import kotlinx.coroutines.flow.first

class QuranRepositoryImpl(
    private val apiService: QuranApiService,
    private val ayahDao: AyahDao,
    private val appPreferences: AppPreferences
) : QuranRepository {

    override suspend fun syncQuranIfNeeded() {
        val synced = appPreferences.quranSynced.first()
        if (synced && ayahDao.countAyahs() > 0) return

        val allAyahs = mutableListOf<com.example.allahnamesquran.data.local.entity.AyahEntity>()

        for (page in 1..604) {
            val response = apiService.getQuranPage(page)
            allAyahs += response.data.ayahs.map(AyahMapper::map)
        }

        ayahDao.clearAll()
        ayahDao.insertAll(allAyahs)
        appPreferences.setQuranSynced(true)
    }

    override suspend fun isOnboardingSeen(): Boolean {
        return appPreferences.onboardingSeen.first()
    }

    override suspend fun setOnboardingSeen() {
        appPreferences.setOnboardingSeen(true)
    }
}