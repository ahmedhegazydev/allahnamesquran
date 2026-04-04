package app.asmaquran.mobile.features.splash

import app.asmaquran.mobile.testutil.FakeAuthRepository
import app.asmaquran.mobile.testutil.FakeDailyNameReminderScheduler
import app.asmaquran.mobile.testutil.FakeQuranRepository
import app.asmaquran.mobile.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: FakeQuranRepository
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var scheduler: FakeDailyNameReminderScheduler

    @Before
    fun setUp() {
        repository = FakeQuranRepository()
        authRepository = FakeAuthRepository()
        scheduler = FakeDailyNameReminderScheduler()
    }

    @Test
    fun `start navigates to onboarding when onboarding was not seen`() = runTest {
        repository.onboardingSeen = false

        val viewModel = SplashViewModel(repository, authRepository, scheduler)
        viewModel.onIntent(SplashIntent.Start)
        advanceUntilIdle()

        assertEquals(SplashDestination.ONBOARDING, viewModel.state.value.destination)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(1, repository.syncCalls)
        assertEquals(1, scheduler.scheduleCalls)
    }

    @Test
    fun `start navigates to home when active session exists`() = runTest {
        repository.onboardingSeen = true
        authRepository.hasActiveSession = true

        val viewModel = SplashViewModel(repository, authRepository, scheduler)
        viewModel.onIntent(SplashIntent.Start)
        advanceUntilIdle()

        assertEquals(SplashDestination.HOME, viewModel.state.value.destination)
    }

    @Test
    fun `start navigates to home when auth prompt is already completed`() = runTest {
        repository.onboardingSeen = true
        authRepository.authPromptCompleted = true

        val viewModel = SplashViewModel(repository, authRepository, scheduler)
        viewModel.onIntent(SplashIntent.Start)
        advanceUntilIdle()

        assertEquals(SplashDestination.HOME, viewModel.state.value.destination)
    }

    @Test
    fun `start navigates to sign in when onboarding is seen and auth is pending`() = runTest {
        repository.onboardingSeen = true

        val viewModel = SplashViewModel(repository, authRepository, scheduler)
        viewModel.onIntent(SplashIntent.Start)
        advanceUntilIdle()

        assertEquals(SplashDestination.SIGN_IN, viewModel.state.value.destination)
    }

    @Test
    fun `start falls back to onboarding when an exception happens`() = runTest {
        repository.syncException = IllegalStateException("sync failed")

        val viewModel = SplashViewModel(repository, authRepository, scheduler)
        viewModel.onIntent(SplashIntent.Start)
        advanceUntilIdle()

        assertEquals(SplashDestination.ONBOARDING, viewModel.state.value.destination)
        assertFalse(viewModel.state.value.isLoading)
    }
}
