package com.example.allahnamesquran.data.mapper

import com.example.allahnamesquran.core.utils.ArabicTextNormalizer
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
            textNormalized = ArabicTextNormalizer.normalize(dto.text)
        )
    }
}