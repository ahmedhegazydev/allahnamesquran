package com.example.allahnamesquran.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahnamesquran.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: QuranRepository
) : ViewModel() {

    val state: StateFlow<HomeUiState>
    field = MutableStateFlow(HomeUiState())

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadData -> loadData()
            is HomeIntent.SearchChanged -> updateSearch(intent.query)
            is HomeIntent.TabSelected -> updateTab(intent.tab)
            is HomeIntent.FavoriteClicked -> toggleFavorite(intent.id)
        }
    }

    private fun loadData() {
        if (state.value.names.isNotEmpty()) return

        viewModelScope.launch {
            state.update {
                it.copy(isLoading = true, isEmpty = false)
            }

            val favoriteIds = repository.getFavoriteNameIds()

            val allNames = repository.getAllAllahNames().map { name ->
                NameUiModel(
                    id = name.id,
                    name = name.name,
                    description = name.description,
                    ayahCount = repository.searchAyahsByAllahName(name.name).size,
                    isFavorite = name.id in favoriteIds
                )
            }

            val visible = filterNames(allNames, HomeTab.ALL, "")
            state.update {
                it.copy(
                    names = allNames,
                    visibleNames = visible,
                    isEmpty = visible.isEmpty(),
                    isLoading = false
                )
            }
        }
    }

    private fun updateSearch(query: String) {
        state.update { current ->
            val visible = filterNames(current.names, current.selectedTab, query)
            current.copy(
                searchQuery = query,
                visibleNames = visible,
                isEmpty = visible.isEmpty()
            )
        }
    }

    private fun updateTab(tab: HomeTab) {
        state.update { current ->
            val visible = filterNames(current.names, tab, current.searchQuery)
            current.copy(
                selectedTab = tab,
                visibleNames = visible,
                isEmpty = visible.isEmpty()
            )
        }
    }

    private fun toggleFavorite(id: Int) {
        viewModelScope.launch {
            val target = state.value.names.firstOrNull { it.id == id } ?: return@launch
            val newFavoriteState = !target.isFavorite
            repository.setFavoriteName(id, newFavoriteState)

            state.update { current ->
                val updatedNames = current.names.map {
                    if (it.id == id) it.copy(isFavorite = newFavoriteState) else it
                }
                val visible = filterNames(updatedNames, current.selectedTab, current.searchQuery)
                current.copy(
                    names = updatedNames,
                    visibleNames = visible,
                    isEmpty = visible.isEmpty()
                )
            }
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
            it.name.contains(query)
        }
    }
}
