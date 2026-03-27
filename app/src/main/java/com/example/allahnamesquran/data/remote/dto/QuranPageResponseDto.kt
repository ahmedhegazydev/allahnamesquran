package com.example.allahnamesquran.data.remote.dto

import com.google.gson.annotations.SerializedName

data class QuranPageResponseDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: QuranPageDataDto
)

data class QuranPageDataDto(
    @SerializedName("number")
    val number: Int,
    @SerializedName("ayahs")
    val ayahs: List<AyahDto>
)

data class AyahDto(
    @SerializedName("number")
    val number: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("surah")
    val surah: SurahDto,
    @SerializedName("numberInSurah")
    val numberInSurah: String,
    @SerializedName("juz")
    val juz: String,
    @SerializedName("page")
    val page: String,
    @SerializedName("hizbQuarter")
    val hizbQuarter: String,
    @SerializedName("sajda")
    val sajda: Any? // Changed to Any? as sajda can be boolean or object/string in some APIs
)

data class SurahDto(
    @SerializedName("number")
    val number: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("englishName")
    val englishName: String,
    @SerializedName("revelationType")
    val revelationType: String
)
