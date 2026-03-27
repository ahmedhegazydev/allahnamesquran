package com.example.allahnamesquran.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuranPageResponseDto(
    val code: Int,
    val status: String,
    val data: QuranPageDataDto
)

@Serializable
data class QuranPageDataDto(
    val number: Int,
    val ayahs: List<AyahDto>
)

@Serializable
data class AyahDto(
    val number: String,
    val text: String,
    val surah: SurahDto,
    val numberInSurah: String,
    val juz: String,
    val page: String,
    val hizbQuarter: String,
    val sajda: String
)

@Serializable
data class SurahDto(
    val number: String,
    val name: String,
    val englishName: String,
    val revelationType: String
)