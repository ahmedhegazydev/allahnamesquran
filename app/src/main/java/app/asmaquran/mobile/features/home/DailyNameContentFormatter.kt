package app.asmaquran.mobile.features.home

import app.asmaquran.mobile.data.model.AllahName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DailyNameContentFormatter {

    private val arabicLocale = Locale.forLanguageTag("ar")
    private val fillerPrefixes = listOf(
        "هو الذي",
        "هو",
        "وهو",
        "معناه",
        "ومعناه",
        "فهو"
    )

    fun formatDate(date: Date = Date()): String {
        return SimpleDateFormat("EEEE، d MMMM yyyy", arabicLocale).format(date)
    }

    fun buildShortDescription(description: String): String {
        val normalized = description
            .replace("، والنفوس", " والنفوس")
            .substringBefore("، فهو")
            .substringBefore(".")
            .trim()

        return fillerPrefixes.fold(normalized) { current, prefix ->
            current.removePrefix(prefix).trim()
        }.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(arabicLocale) else char.toString()
        }
    }

    fun buildReflection(name: AllahName): String {
        val summary = buildShortDescription(name.description)
            .trimEnd('،', '؛', '.')

        return "تأمل اليوم مع اسم ${name.name}: استحضر أن الله $summary، " +
            "واسأله أن يفتح لك من أثر هذا الاسم طمأنينةً وهدايةً في يومك."
    }

    fun buildNotificationBody(name: AllahName): String {
        val summary = buildShortDescription(name.description)
            .trimEnd('،', '؛', '.')

        return "اسم اليوم ${name.name}: $summary."
    }
}
