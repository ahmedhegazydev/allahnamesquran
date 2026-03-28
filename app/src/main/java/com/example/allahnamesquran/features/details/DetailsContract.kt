package com.example.allahnamesquran.features.details

import androidx.compose.runtime.Immutable

@Immutable
data class AyahUiModel(
    val id: Int,
    val surahName: String,
    val ayahNumber: Int,
    val page: Int,
    val juz: Int,
    val text: String
)

@Immutable
data class DetailsUiState(
    val isLoading: Boolean = false,
    val nameId: Int? = null,
    val name: String = "",
    val englishName: String = "",
    val description: String = "",
    val ayahsCount: Int = 0,
    val ayahs: List<AyahUiModel> = emptyList(),
    val isFavorite: Boolean = false,
    val error: String? = null
)

sealed interface DetailsIntent {
    data class LoadData(val nameId: Int) : DetailsIntent
    data object ToggleFavorite : DetailsIntent
}