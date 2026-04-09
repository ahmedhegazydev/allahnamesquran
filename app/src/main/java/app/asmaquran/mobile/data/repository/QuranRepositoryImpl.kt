package app.asmaquran.mobile.data.repository

import app.asmaquran.mobile.core.utils.ArabicTextNormalizer
import app.asmaquran.mobile.data.local.dao.AyahDao
import app.asmaquran.mobile.data.mapper.AyahMapper
import app.asmaquran.mobile.data.model.AllahName
import app.asmaquran.mobile.data.model.AyahSearchResult
import app.asmaquran.mobile.data.preferences.AppPreferences
import app.asmaquran.mobile.data.remote.QuranApiService
import app.asmaquran.mobile.data.static.AllahNamesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class QuranRepositoryImpl(
    private val apiService: QuranApiService,
    private val ayahDao: AyahDao,
    private val appPreferences: AppPreferences
) : QuranRepository {

    override suspend fun syncQuranIfNeeded() {
        val synced = appPreferences.quranSynced.first()
        if (synced && ayahDao.countAyahs() > 0) return

        val allAyahs = mutableListOf<app.asmaquran.mobile.data.local.entity.AyahEntity>()

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

    override fun getAllAllahNames(): List<AllahName> {
        return AllahNamesDataSource.allNames
    }

    override fun getAllahNameById(id: Int): AllahName? {
        return AllahNamesDataSource.allNames.firstOrNull { it.id == id }
    }

    override fun observeFavoriteNameIds(): Flow<Set<Int>> {
        return appPreferences.favoriteNameIds
    }

    override suspend fun getFavoriteNameIds(): Set<Int> {
        return appPreferences.favoriteNameIds.first()
    }

    override suspend fun setFavoriteName(id: Int, isFavorite: Boolean) {
        appPreferences.setFavorite(nameId = id, isFavorite = isFavorite)
    }

    override suspend fun searchAyahsByAllahName(name: String): List<AyahSearchResult> {
        val normalizedName = ArabicTextNormalizer.normalize(name)
        if (normalizedName.isBlank()) return emptyList()

        return ayahDao.searchAyahsByName(normalizedName).map {
            AyahSearchResult(
                id = it.globalAyahNumber,
                surahName = it.surahName,
                ayahNumber = it.ayahNumberInSurah,
                page = it.page,
                juz = it.juz,
                text = it.textOriginal
            )
        }
    }
}
