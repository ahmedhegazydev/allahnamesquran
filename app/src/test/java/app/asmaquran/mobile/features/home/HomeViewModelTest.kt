package app.asmaquran.mobile.features.home

import app.asmaquran.mobile.testutil.FakeQuranRepository
import app.asmaquran.mobile.testutil.MainDispatcherRule
import app.asmaquran.mobile.testutil.alAleem
import app.asmaquran.mobile.testutil.alAleemAyah
import app.asmaquran.mobile.testutil.arRahman
import app.asmaquran.mobile.testutil.arRahmanAyah
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeQuranRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakeQuranRepository().apply {
            names = listOf(alAleem, arRahman)
            favoriteIds = mutableSetOf(alAleem.id)
            ayahsByName = mapOf(
                alAleem.name to listOf(alAleemAyah),
                arRahman.name to listOf(arRahmanAyah)
            )
        }
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `loadData populates names favorites and daily name`() = runTest {
        viewModel.onIntent(HomeIntent.LoadData)
        advanceUntilIdle()

        val state = viewModel.state.value
        val expectedDailyName = DailyNameFactory.getNameForDate(repository.names)

        assertFalse(state.isLoading)
        assertFalse(state.isEmpty)
        assertEquals(listOf(alAleem.id, arRahman.id), state.names.map { it.id })
        assertEquals(listOf(alAleem.id, arRahman.id), state.visibleNames.map { it.id })
        assertTrue(state.names.first { it.id == alAleem.id }.isFavorite)
        assertNotNull(state.dailyName)
        assertEquals(expectedDailyName?.id, state.dailyName?.id)
        assertEquals(
            repository.ayahsByName[expectedDailyName?.name].orEmpty().firstOrNull()?.text,
            state.dailyName?.ayahText
        )
    }

    @Test
    fun `searchChanged filters visible names`() = runTest {
        viewModel.onIntent(HomeIntent.LoadData)
        advanceUntilIdle()

        viewModel.onIntent(HomeIntent.SearchChanged("العليم"))

        val state = viewModel.state.value
        assertEquals("العليم", state.searchQuery)
        assertEquals(listOf(alAleem.id), state.visibleNames.map { it.id })
        assertFalse(state.isEmpty)
    }

    @Test
    fun `tabSelected and favoriteClicked update favorites list`() = runTest {
        viewModel.onIntent(HomeIntent.LoadData)
        advanceUntilIdle()

        viewModel.onIntent(HomeIntent.TabSelected(HomeTab.FAVORITES))
        assertEquals(listOf(alAleem.id), viewModel.state.value.visibleNames.map { it.id })

        viewModel.onIntent(HomeIntent.FavoriteClicked(arRahman.id))
        advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(listOf(alAleem.id, arRahman.id), state.visibleNames.map { it.id })
        assertTrue(state.names.first { it.id == arRahman.id }.isFavorite)
        assertEquals(listOf(arRahman.id to true), repository.favoriteUpdates)
    }

    @Test
    fun `loadData does not reload when names already exist`() = runTest {
        viewModel.onIntent(HomeIntent.LoadData)
        advanceUntilIdle()
        val initialCalls = repository.getAllAllahNamesCalls

        viewModel.onIntent(HomeIntent.LoadData)
        advanceUntilIdle()

        assertEquals(initialCalls, repository.getAllAllahNamesCalls)
    }

    @Test
    fun `external favorite changes refresh visible favorites and empty state`() = runTest {
        viewModel.onIntent(HomeIntent.LoadData)
        advanceUntilIdle()

        viewModel.onIntent(HomeIntent.TabSelected(HomeTab.FAVORITES))
        assertEquals(listOf(alAleem.id), viewModel.state.value.visibleNames.map { it.id })

        repository.emitFavoriteIds(emptySet())
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state.visibleNames.isEmpty())
        assertTrue(state.isEmpty)
        assertFalse(state.names.first { it.id == alAleem.id }.isFavorite)
    }
}
