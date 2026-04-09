package app.asmaquran.mobile.data.repository

import app.asmaquran.mobile.data.model.AllahName
import app.asmaquran.mobile.data.model.AyahSearchResult
import kotlinx.coroutines.flow.Flow

interface QuranRepository {
    suspend fun syncQuranIfNeeded()
    suspend fun isOnboardingSeen(): Boolean
    suspend fun setOnboardingSeen()
    fun getAllAllahNames(): List<AllahName>
    fun getAllahNameById(id: Int): AllahName?
    fun observeFavoriteNameIds(): Flow<Set<Int>>
    suspend fun getFavoriteNameIds(): Set<Int>
    suspend fun setFavoriteName(id: Int, isFavorite: Boolean)
    suspend fun searchAyahsByAllahName(name: String): List<AyahSearchResult>
}
