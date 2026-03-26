package com.example.allahnamesquran.features.details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailsViewModel : ViewModel() {

    private val _state = MutableStateFlow(DetailsUiState())
    val state: StateFlow<DetailsUiState> = _state.asStateFlow()

    fun onIntent(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadData -> loadData(intent.nameId)
            DetailsIntent.ToggleFavorite -> toggleFavorite()
        }
    }

    private fun loadData(nameId: Int) {
        val state = when (nameId) {
            1 -> DetailsUiState(
                nameId = 1,
                name = "الرَّحْمَنُ",
                transliteration = "Ar-Rahman",
                meaning = "الواسع الرحمة",
                description = "هو كثير الرحمة الذي وسعت رحمته كل شيء، وهو اسم مختص بالله سبحانه وتعالى.",
                ayahs = listOf(
                    AyahUiModel(
                        id = 1,
                        surahName = "سورة الفاتحة",
                        ayahNumber = 3,
                        text = "ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
                    ),
                    AyahUiModel(
                        id = 2,
                        surahName = "سورة طه",
                        ayahNumber = 5,
                        text = "ٱلرَّحْمَٰنُ عَلَى ٱلْعَرْشِ ٱسْتَوَىٰ"
                    ),
                    AyahUiModel(
                        id = 3,
                        surahName = "سورة الملك",
                        ayahNumber = 29,
                        text = "قُلْ هُوَ ٱلرَّحْمَٰنُ آمَنَّا بِهِ وَعَلَيْهِ تَوَكَّلْنَا"
                    )
                ),
                ayahsCount = 3,
                isFavorite = true
            )

            2 -> DetailsUiState(
                nameId = 2,
                name = "الرَّحِيمُ",
                transliteration = "Ar-Raheem",
                meaning = "المنعم على عباده",
                description = "هو الذي يوصل رحمته لعباده المؤمنين، ورحمته لا تنقطع.",
                ayahs = listOf(
                    AyahUiModel(
                        id = 4,
                        surahName = "سورة الفاتحة",
                        ayahNumber = 3,
                        text = "ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
                    ),
                    AyahUiModel(
                        id = 5,
                        surahName = "سورة الأحزاب",
                        ayahNumber = 43,
                        text = "وَكَانَ بِٱلْمُؤْمِنِينَ رَحِيمًا"
                    )
                ),
                ayahsCount = 2,
                isFavorite = true
            )

            else -> DetailsUiState(
                nameId = nameId,
                name = "الاسم",
                transliteration = "Name",
                meaning = "المعنى",
                description = "وصف مختصر للاسم.",
                ayahs = listOf(
                    AyahUiModel(
                        id = 100 + nameId,
                        surahName = "سورة الفاتحة",
                        ayahNumber = 1,
                        text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
                    )
                ),
                ayahsCount = 1,
                isFavorite = false
            )
        }

        _state.value = state
    }

    private fun toggleFavorite() {
        _state.update {
            it.copy(isFavorite = !it.isFavorite)
        }
    }
}