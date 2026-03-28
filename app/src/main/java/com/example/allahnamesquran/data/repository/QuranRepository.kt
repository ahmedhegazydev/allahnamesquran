package com.example.allahnamesquran.data.repository

import com.example.allahnamesquran.data.model.AllahName
import com.example.allahnamesquran.data.model.AyahSearchResult

interface QuranRepository {
    suspend fun syncQuranIfNeeded()
    suspend fun isOnboardingSeen(): Boolean
    suspend fun setOnboardingSeen()
    fun getAllAllahNames(): List<AllahName>
    fun getAllahNameById(id: Int): AllahName?
    suspend fun getFavoriteNameIds(): Set<Int>
    suspend fun setFavoriteName(id: Int, isFavorite: Boolean)
    suspend fun searchAyahsByAllahName(name: String): List<AyahSearchResult>
}