package com.example.allahnamesquran.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ayahs")
data class AyahEntity(
    @PrimaryKey
    val globalAyahNumber: Int,
    val surahNumber: Int,
    val surahName: String,
    val ayahNumberInSurah: Int,
    val juz: Int,
    val page: Int,
    val hizbQuarter: Int,
    val textOriginal: String,
    val textNormalized: String
)