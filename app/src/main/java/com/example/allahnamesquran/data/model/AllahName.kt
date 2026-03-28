package com.example.allahnamesquran.data.model

data class AllahName(
    val id: Int,
    val name: String,
    val description: String
)

data class AyahSearchResult(
    val id: Int,
    val surahName: String,
    val ayahNumber: Int,
    val page: Int,
    val juz: Int,
    val text: String
)