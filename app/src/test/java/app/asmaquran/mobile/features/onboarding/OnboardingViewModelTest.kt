package app.asmaquran.mobile.features.onboarding

import app.asmaquran.mobile.testutil.FakeQuranRepository
import app.asmaquran.mobile.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `onStartClicked marks onboarding as seen and emits navigation`() = runTest {
        val repository = FakeQuranRepository()
        val viewModel = OnboardingViewModel(repository)
        val navigation = async { viewModel.navigationEvents.first() }

        viewModel.onStartClicked()
        advanceUntilIdle()

        assertEquals(1, repository.setOnboardingSeenCalls)
        assertTrue(repository.onboardingSeen)
        assertEquals(Unit, navigation.await())
    }
}
