package app.asmaquran.mobile.core.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.asmaquran.mobile.core.ui.theme.AllahNamesQuranTheme
import app.asmaquran.mobile.features.details.AyahUiModel
import app.asmaquran.mobile.features.details.DetailsUiState
import app.asmaquran.mobile.features.home.DailyNameUiModel
import app.asmaquran.mobile.features.home.HomeTab
import app.asmaquran.mobile.features.home.HomeUiState
import app.asmaquran.mobile.features.home.NameUiModel

val PreviewNameItems = listOf(
    NameUiModel(
        id = 1,
        name = "اللَّهُ",
        description = "هو الاسم الأعظم الذي تفرد به سبحانه، الجامع لجميع صفات الكمال والجلال.",
        ayahCount = 2699,
        isFavorite = true
    ),
    NameUiModel(
        id = 2,
        name = "الرَّحْمٰنُ",
        description = "الواسع الرحمة الذي عمَّت رحمته جميع الخلق في الدنيا والآخرة.",
        ayahCount = 57,
        isFavorite = false
    ),
    NameUiModel(
        id = 3,
        name = "الرَّحِيمُ",
        description = "الذي يخص عباده المؤمنين بتمام رحمته وإحسانه وهدايته.",
        ayahCount = 114,
        isFavorite = true
    ),
    NameUiModel(
        id = 4,
        name = "المَلِكُ",
        description = "المالك لكل شيء، المتصرف في خلقه بما شاء، الكامل الملك والسلطان.",
        ayahCount = 12,
        isFavorite = false
    )
)

val PreviewAyahs = listOf(
    AyahUiModel(
        id = 1,
        surahName = "الفاتحة",
        ayahNumber = 1,
        page = 1,
        juz = 1,
        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
    ),
    AyahUiModel(
        id = 2,
        surahName = "البقرة",
        ayahNumber = 255,
        page = 42,
        juz = 3,
        text = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ"
    )
)

val PreviewHomeUiState = HomeUiState(
    searchQuery = "الل",
    selectedTab = HomeTab.ALL,
    names = PreviewNameItems,
    visibleNames = PreviewNameItems,
    dailyName = DailyNameUiModel(
        id = 3,
        dateText = "الأحد، 29 مارس 2026",
        name = "العَلِيمُ",
        englishName = "Al-Alim",
        shortDescription = "العالم بكل شيء، لا يخفى عليه ظاهر ولا باطن، ولا يغيب عن علمه شيء.",
        reflection = "العالم بما كان وما يكون وما لم يكن لو كان كيف يكون.",
        ayahText = "إِنَّ اللَّهَ كَانَ عَلِيمًا حَكِيمًا",
        ayahReference = "النساء - آية 11"
    ),
    isLoading = false,
    isEmpty = false
)

val PreviewFavoriteHomeUiState = PreviewHomeUiState.copy(
    selectedTab = HomeTab.FAVORITES,
    visibleNames = PreviewNameItems.filter { it.isFavorite }
)

val PreviewDetailsUiState = DetailsUiState(
    nameId = 1,
    name = "اللَّهُ",
    englishName = "ALLAH",
    description = "هو الاسم الدال على الذات الإلهية الجامعة لجميع صفات الكمال، المنفرد بالألوهية والعبادة.",
    ayahsCount = PreviewAyahs.size,
    ayahs = PreviewAyahs,
    isFavorite = true
)

@Composable
fun PreviewSurface(content: @Composable () -> Unit) {
    AllahNamesQuranTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            content()
        }
    }
}
