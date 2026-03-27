package com.example.allahnamesquran.data.mapper

import com.example.allahnamesquran.data.local.entity.AyahEntity
import com.example.allahnamesquran.data.remote.dto.AyahDto

object AyahMapper {

    fun map(dto: AyahDto): AyahEntity {
        return AyahEntity(
            globalAyahNumber = dto.number.toInt(),
            surahNumber = dto.surah.number.toInt(),
            surahName = dto.surah.name,
            ayahNumberInSurah = dto.numberInSurah.toInt(),
            juz = dto.juz.toInt(),
            page = dto.page.toInt(),
            hizbQuarter = dto.hizbQuarter.toInt(),
            textOriginal = dto.text,
            textNormalized = normalizeArabic(dto.text)
        )
    }

    private fun normalizeArabic(input: String): String {
        return input
            .replace("ﷲ", "الله")
            .replace(Regex("[\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]"), "")
            .replace("ٱ", "ا")
            .replace(Regex("[أإآ]"), "ا")
            .replace("ى", "ي")
            .replace("ؤ", "و")
            .replace("ئ", "ي")
            .replace("ـ", "")
            .replace(Regex("[^\\p{IsArabic}\\d\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}