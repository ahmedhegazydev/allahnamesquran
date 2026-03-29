package app.asmaquran.mobile.features.home

import app.asmaquran.mobile.data.model.AyahSearchResult
import app.asmaquran.mobile.data.model.AllahName
import app.asmaquran.mobile.data.static.AllahNamesTranslations
import java.util.Calendar
import java.util.Date

object DailyNameFactory {

    fun getNameForDate(
        names: List<AllahName>,
        date: Date = Date()
    ): AllahName? {
        if (names.isEmpty()) return null

        val calendar = Calendar.getInstance().apply { time = date }
        return names[(calendar.get(Calendar.DAY_OF_YEAR) - 1) % names.size]
    }

    fun buildUiModel(
        name: AllahName,
        ayah: AyahSearchResult?,
        date: Date = Date()
    ): DailyNameUiModel {
        return DailyNameUiModel(
            id = name.id,
            dateText = DailyNameContentFormatter.formatDate(date),
            name = name.name,
            englishName = AllahNamesTranslations.getEnglishName(name.id, name.name),
            shortDescription = DailyNameContentFormatter.buildShortDescription(name.description),
            reflection = DailyNameContentFormatter.buildReflection(name),
            ayahText = ayah?.text ?: "لا توجد آيات متاحة لهذا الاسم الآن.",
            ayahReference = ayah?.let { "${it.surahName} - آية ${it.ayahNumber}" }
                ?: "سيظهر المرجع عند توفر آية مرتبطة"
        )
    }
}
