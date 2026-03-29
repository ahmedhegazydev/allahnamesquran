package com.example.allahnamesquran.features.home

import androidx.compose.runtime.Immutable

@Immutable
data class NameUiModel(
    val id: Int,
    val name: String,
    val description: String,
    val ayahCount: Int,
    val isFavorite: Boolean
)

@Immutable
data class DailyNameUiModel(
    val id: Int,
    val dateText: String,
    val name: String,
    val englishName: String,
    val shortDescription: String,
    val reflection: String,
    val ayahText: String,
    val ayahReference: String
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
    val dailyName: DailyNameUiModel? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)

sealed interface HomeIntent {
    data object LoadData : HomeIntent
    data class SearchChanged(val query: String) : HomeIntent
    data class TabSelected(val tab: HomeTab) : HomeIntent
    data class FavoriteClicked(val id: Int) : HomeIntent
}
