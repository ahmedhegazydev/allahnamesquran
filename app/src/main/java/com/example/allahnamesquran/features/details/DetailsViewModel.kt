package com.example.allahnamesquran.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.allahnamesquran.data.repository.QuranRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: QuranRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state: StateFlow<DetailsUiState> = _state.asStateFlow()

    fun onIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadData -> loadData(intent.nameId)
            DetailsIntent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun loadData(nameId: Int) {
        viewModelScope.launch {
            val favoriteIds = repository.getFavoriteNameIds()
            val name = repository.getAllahNameById(nameId)
            val ayahs = name?.let { repository.searchAyahsByAllahName(it.name) }.orEmpty()

            _state.value = DetailsUiState(
                nameId = nameId,
                name = name?.name.orEmpty(),
                description = name?.description.orEmpty(),
                ayahs = ayahs.map {
                    AyahUiModel(
                        id = it.id,
                        surahName = it.surahName,
                        ayahNumber = it.ayahNumber,
                        text = it.text
                    )
                },
                ayahsCount = ayahs.size,
                isFavorite = nameId in favoriteIds
            )
        }
    }

    private fun toggleFavorite() {
        viewModelScope.launch {
            val current = _state.value
            val nameId = current.nameId ?: return@launch
            val newFavoriteState = !current.isFavorite

            repository.setFavoriteName(nameId, newFavoriteState)

            _state.update {
                it.copy(isFavorite = newFavoriteState)
            }
        }
    }
}