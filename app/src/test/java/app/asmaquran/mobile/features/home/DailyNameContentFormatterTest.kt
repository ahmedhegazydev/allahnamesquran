package app.asmaquran.mobile.features.home

import app.asmaquran.mobile.testutil.alAleem
import java.util.Calendar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DailyNameContentFormatterTest {

    @Test
    fun `formatDate returns arabic date text`() {
        val calendar = Calendar.getInstance().apply {
            set(2025, Calendar.JANUARY, 2, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val result = DailyNameContentFormatter.formatDate(calendar.time)

        assertTrue(result.contains("يناير"))
        assertTrue(result.contains("،"))
    }

    @Test
    fun `buildShortDescription removes filler prefixes and trailing clauses`() {
        val result = DailyNameContentFormatter.buildShortDescription(alAleem.description)

        assertEquals("يعلم كل شيء", result)
    }

    @Test
    fun `buildReflection uses normalized short description`() {
        val result = DailyNameContentFormatter.buildReflection(alAleem)

        assertEquals(
            "تأمل اليوم مع اسم العليم: استحضر أن الله يعلم كل شيء، واسأله أن يفتح لك من أثر هذا الاسم طمأنينةً وهدايةً في يومك.",
            result
        )
    }

    @Test
    fun `buildNotificationBody uses normalized short description`() {
        val result = DailyNameContentFormatter.buildNotificationBody(alAleem)

        assertEquals("اسم اليوم العليم: يعلم كل شيء.", result)
    }
}
