package com.example.allahnamesquran.features.home

import androidx.compose.runtime.Immutable

@Immutable
data class NameUiModel(
    val id: Int,
    val name: String,
    val meaning: String,
    val ayahCount: Int,
    val isFavorite: Boolean
)

enum class HomeTab {
    ALL,
    FAVORITES
}

@Immutable
data class HomeUiState(
    val searchQuery: String = "",
    val selectedTab: HomeTab = HomeTab.ALL,
    val names: List<NameUiModel> = emptyList(),
    val visibleNames: List<NameUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)

sealed interface HomeIntent {
    data object LoadData : HomeIntent
    data class SearchChanged(val query: String) : HomeIntent
    data class TabSelected(val tab: HomeTab) : HomeIntent
    data class FavoriteClicked(val id: Int) : HomeIntent
}