package com.example.allahnamesquran.features.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val seedNames = listOf(
        NameUiModel(1, "الرَّحْمَنُ", "الرحيم", 4, true),
        NameUiModel(2, "الرَّحِيمُ", "الرحيم بالمؤمنين", 3, true),
        NameUiModel(3, "الْقُدُّوسُ", "المنزه عن النقائص", 2, false),
        NameUiModel(4, "السَّلَامُ", "السالم من كل عيب", 1, false),
        NameUiModel(5, "الْمَلِكُ", "المالك لكل شيء", 2, false),
        NameUiModel(6, "التَّوَّابُ", "القابل للتوبة", 2, true)
    )

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadData -> loadData()
            is HomeIntent.SearchChanged -> updateSearch(intent.query)
            is HomeIntent.TabSelected -> updateTab(intent.tab)
            is HomeIntent.FavoriteClicked -> toggleFavorite(intent.id)
        }
    }

    private fun loadData() {
        val visible = filterNames(seedNames, HomeTab.ALL, "")
        _state.update {
            it.copy(
                names = seedNames,
                visibleNames = visible,
                isEmpty = visible.isEmpty()
            )
        }
    }

    private fun updateSearch(query: String) {
        _state.update { current ->
            val visible = filterNames(current.names, current.selectedTab, query)
            current.copy(
                searchQuery = query,
                visibleNames = visible,
                isEmpty = visible.isEmpty()
            )
        }
    }

    private fun updateTab(tab: HomeTab) {
        _state.update { current ->
            val visible = filterNames(current.names, tab, current.searchQuery)
            current.copy(
                selectedTab = tab,
                visibleNames = visible,
                isEmpty = visible.isEmpty()
            )
        }
    }

    private fun toggleFavorite(id: Int) {
        _state.update { current ->
            val updatedNames = current.names.map {
                if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it
            }
            val visible = filterNames(updatedNames, current.selectedTab, current.searchQuery)
            current.copy(
                names = updatedNames,
                visibleNames = visible,
                isEmpty = visible.isEmpty()
            )
        }
    }

    private fun filterNames(
        names: List<NameUiModel>,
        tab: HomeTab,
        query: String
    ): List<NameUiModel> {
        val source = if (tab == HomeTab.FAVORITES) {
            names.filter { it.isFavorite }
        } else {
            names
        }

        if (query.isBlank()) return source

        return source.filter {
            it.name.contains(query) || it.meaning.contains(query)
        }
    }
}