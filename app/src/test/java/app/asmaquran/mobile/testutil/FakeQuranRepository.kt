package app.asmaquran.mobile.testutil

import app.asmaquran.mobile.data.model.AllahName
import app.asmaquran.mobile.data.model.AyahSearchResult
import app.asmaquran.mobile.data.repository.QuranRepository

class FakeQuranRepository : QuranRepository {

    var syncCalls = 0
    var syncException: Exception? = null
    var onboardingSeen = false
    var setOnboardingSeenCalls = 0
    var getAllAllahNamesCalls = 0
    var getFavoriteNameIdsCalls = 0
    val favoriteUpdates = mutableListOf<Pair<Int, Boolean>>()
    val searchQueries = mutableListOf<String>()

    var names: List<AllahName> = emptyList()
    var favoriteIds: MutableSet<Int> = mutableSetOf()
    var ayahsByName: Map<String, List<AyahSearchResult>> = emptyMap()

    override suspend fun syncQuranIfNeeded() {
        syncCalls++
        syncException?.let { throw it }
    }

    override suspend fun isOnboardingSeen(): Boolean = onboardingSeen

    override suspend fun setOnboardingSeen() {
        setOnboardingSeenCalls++
        onboardingSeen = true
    }

    override fun getAllAllahNames(): List<AllahName> {
        getAllAllahNamesCalls++
        return names
    }

    override fun getAllahNameById(id: Int): AllahName? {
        return names.firstOrNull { it.id == id }
    }

    override suspend fun getFavoriteNameIds(): Set<Int> {
        getFavoriteNameIdsCalls++
        return favoriteIds.toSet()
    }

    override suspend fun setFavoriteName(id: Int, isFavorite: Boolean) {
        favoriteUpdates += id to isFavorite
        if (isFavorite) {
            favoriteIds += id
        } else {
            favoriteIds -= id
        }
    }

    override suspend fun searchAyahsByAllahName(name: String): List<AyahSearchResult> {
        searchQueries += name
        return ayahsByName[name].orEmpty()
    }
}
