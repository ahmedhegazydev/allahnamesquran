package app.asmaquran.mobile.features.home

import app.asmaquran.mobile.testutil.alAleem
import app.asmaquran.mobile.testutil.alAleemAyah
import app.asmaquran.mobile.testutil.arRahman
import java.util.Calendar
import java.util.Date
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DailyNameFactoryTest {

    @Test
    fun `getNameForDate returns day indexed item and wraps around`() {
        val names = listOf(alAleem, arRahman)

        assertEquals(alAleem, DailyNameFactory.getNameForDate(names, dateOf(2025, Calendar.JANUARY, 1)))
        assertEquals(arRahman, DailyNameFactory.getNameForDate(names, dateOf(2025, Calendar.JANUARY, 2)))
        assertEquals(alAleem, DailyNameFactory.getNameForDate(names, dateOf(2025, Calendar.JANUARY, 3)))
    }

    @Test
    fun `getNameForDate returns null when names are empty`() {
        assertNull(DailyNameFactory.getNameForDate(emptyList(), dateOf(2025, Calendar.JANUARY, 1)))
    }

    @Test
    fun `buildUiModel maps translated name reflection and ayah reference`() {
        val result = DailyNameFactory.buildUiModel(
            name = alAleem,
            ayah = alAleemAyah,
            date = dateOf(2025, Calendar.JANUARY, 2)
        )

        assertEquals(20, result.id)
        assertEquals("العليم", result.name)
        assertEquals("AL-'ALEEM", result.englishName)
        assertEquals("يعلم كل شيء", result.shortDescription)
        assertEquals("إِنَّ اللَّهَ كَانَ عَلِيمًا حَكِيمًا", result.ayahText)
        assertEquals("النساء - آية 11", result.ayahReference)
        assertEquals(
            "تأمل اليوم مع اسم العليم: استحضر أن الله يعلم كل شيء، واسأله أن يفتح لك من أثر هذا الاسم طمأنينةً وهدايةً في يومك.",
            result.reflection
        )
    }

    @Test
    fun `buildUiModel falls back when no ayah is available`() {
        val result = DailyNameFactory.buildUiModel(
            name = arRahman,
            ayah = null,
            date = dateOf(2025, Calendar.JANUARY, 2)
        )

        assertEquals("لا توجد آيات متاحة لهذا الاسم الآن.", result.ayahText)
        assertEquals("سيظهر المرجع عند توفر آية مرتبطة", result.ayahReference)
    }

    private fun dateOf(year: Int, month: Int, day: Int): Date {
        return Calendar.getInstance().apply {
            set(year, month, day, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
    }
}
