package app.asmaquran.mobile.features.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.asmaquran.mobile.data.repository.QuranRepository
import app.asmaquran.mobile.data.static.AllahNamesTranslations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: QuranRepository
) : ViewModel() {

    val state: StateFlow<DetailsUiState> field = MutableStateFlow(DetailsUiState())

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

            state.value = DetailsUiState(
                nameId = nameId,
                name = name?.name.orEmpty(),
                englishName = name?.let { AllahNamesTranslations.getEnglishName(it.id, it.name) }.orEmpty(),
                description = name?.description.orEmpty(),
                ayahs = ayahs.map {
                    AyahUiModel(
                        id = it.id,
                        surahName = it.surahName,
                        ayahNumber = it.ayahNumber,
                        page = it.page,
                        juz = it.juz,
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
            val current = state.value
            val nameId = current.nameId ?: return@launch
            val newFavoriteState = !current.isFavorite

            repository.setFavoriteName(nameId, newFavoriteState)

            state.update {
                it.copy(isFavorite = newFavoriteState)
            }
        }
    }
}
