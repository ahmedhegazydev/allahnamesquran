package com.example.allahnamesquran.data.repository

interface QuranRepository {
    suspend fun syncQuranIfNeeded()
    suspend fun isOnboardingSeen(): Boolean
    suspend fun setOnboardingSeen()
}