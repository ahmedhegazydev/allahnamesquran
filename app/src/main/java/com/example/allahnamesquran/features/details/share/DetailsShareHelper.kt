package com.example.allahnamesquran.features.details.share

import android.content.Context
import android.content.Intent
import com.example.allahnamesquran.features.details.AyahUiModel
import com.example.allahnamesquran.features.details.DetailsUiState

object DetailsShareHelper {

    fun shareText(context: Context, state: DetailsUiState) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "${state.name} | آيات الأسماء الحسنى")
            putExtra(Intent.EXTRA_TEXT, buildShareText(state))
        }

        context.startActivity(Intent.createChooser(shareIntent, "مشاركة الاسم"))
    }

    private fun buildShareText(state: DetailsUiState): String {
        val uniquePages = state.ayahs.map { it.page }.distinct().sorted()
        val uniqueAjzaa = state.ayahs.map { it.juz }.distinct().sorted()
        val previewAyahs = state.ayahs.take(5)
        val locationItems = buildLocationItems(state.ayahs)

        return buildString {
            appendLine("🌿 ${state.name}")

            if (state.englishName.isNotBlank()) {
                appendLine(state.englishName)
            }

            appendLine()
            appendLine("📝 الوصف")
            appendLine(state.description.ifBlank { "لا يوجد وصف متاح لهذا الاسم حاليًا." })
            appendLine()
            appendLine("📊 ملخص سريع")
            appendLine("• عدد الآيات: ${state.ayahsCount}")
            appendLine("• عدد الصفحات: ${uniquePages.size}")
            appendLine("• عدد الأجزاء: ${uniqueAjzaa.size}")

            if (previewAyahs.isNotEmpty()) {
                appendLine()
                appendLine("📖 نماذج من الآيات")
                previewAyahs.forEachIndexed { index, ayah ->
                    appendLine(formatAyahItem(index + 1, ayah))
                }

                if (state.ayahs.size > previewAyahs.size) {
                    appendLine("• ... وهناك ${state.ayahs.size - previewAyahs.size} مواضع أخرى داخل التطبيق")
                }
            }

            if (locationItems.isNotEmpty()) {
                appendLine()
                appendLine("📍 تفاصيل المواضع")
                locationItems.forEach { item ->
                    appendLine(item)
                }
            }

            appendLine()
            append("تمت المشاركة من تطبيق آيات الأسماء الحسنى")
        }.trim()
    }

    private fun buildLocationItems(ayahs: List<AyahUiModel>): List<String> {
        if (ayahs.isEmpty()) return emptyList()

        val maxItems = 12
        val visibleItems = ayahs.take(maxItems).mapIndexed { index, ayah ->
            "${index + 1}) ${ayah.surahName} — آية ${ayah.ayahNumber} • صفحة ${ayah.page} • جزء ${ayah.juz}"
        }

        return if (ayahs.size > maxItems) {
            visibleItems + "... وباقي ${ayahs.size - maxItems} موضع داخل التطبيق"
        } else {
            visibleItems
        }
    }

    private fun formatAyahItem(index: Int, ayah: AyahUiModel): String {
        return buildString {
            appendLine("$index. ${ayah.text}")
            append("   ${ayah.surahName} — آية ${ayah.ayahNumber} • صفحة ${ayah.page} • جزء ${ayah.juz}")
        }
    }
}