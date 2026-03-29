package app.asmaquran.mobile.core.utils

object ArabicTextNormalizer {

    fun normalize(input: String): String {
        return input
            .replace("ﷲ", "الله")
            .replace(Regex("[\\u064B-\\u065F\\u0670\\u06D6-\\u06ED]"), "")
            .replace("ٱ", "ا")
            .replace(Regex("[أإآ]"), "ا")
            .replace("ى", "ي")
            .replace("ؤ", "و")
            .replace("ئ", "ي")
            .replace("ـ", "")
            .replace(Regex("[^\\p{IsArabic}\\d\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}