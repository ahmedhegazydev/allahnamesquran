package app.asmaquran.mobile.testutil

import app.asmaquran.mobile.data.model.AllahName
import app.asmaquran.mobile.data.model.AyahSearchResult

val alAleem = AllahName(
    id = 20,
    name = "العليم",
    description = "هو الذي يعلم كل شيء، فهو واسع الرحمة."
)

val arRahman = AllahName(
    id = 2,
    name = "الرحمن",
    description = "هو كثير الرحمة بعباده."
)

val alAleemAyah = AyahSearchResult(
    id = 1,
    surahName = "النساء",
    ayahNumber = 11,
    page = 78,
    juz = 4,
    text = "إِنَّ اللَّهَ كَانَ عَلِيمًا حَكِيمًا"
)

val arRahmanAyah = AyahSearchResult(
    id = 2,
    surahName = "الفاتحة",
    ayahNumber = 3,
    page = 1,
    juz = 1,
    text = "الرَّحْمَٰنِ الرَّحِيمِ"
)
