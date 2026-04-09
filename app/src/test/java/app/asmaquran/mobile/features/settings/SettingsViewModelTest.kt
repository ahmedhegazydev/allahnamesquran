package app.asmaquran.mobile.features.settings

import app.asmaquran.mobile.data.auth.AuthUserProfile
import app.asmaquran.mobile.testutil.FakeAuthRepository
import app.asmaquran.mobile.testutil.FakeDailyNameReminderScheduler
import app.asmaquran.mobile.testutil.FakeSettingsPreferencesStore
import app.asmaquran.mobile.testutil.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var preferences: FakeSettingsPreferencesStore
    private lateinit var authRepository: FakeAuthRepository
    private lateinit var scheduler: FakeDailyNameReminderScheduler

    @Before
    fun setUp() {
        preferences = FakeSettingsPreferencesStore()
        authRepository = FakeAuthRepository()
        scheduler = FakeDailyNameReminderScheduler()
    }

    @Test
    fun `init loads persisted settings and signed in account`() = runTest {
        preferences.setDailyReminderEnabled(false)
        preferences.setDailyReminderTime(7, 30)
        preferences.setSelectedLanguageTag(SettingsLanguageOption.ENGLISH.storageValue)
        preferences.setSelectedAppearanceMode(SettingsAppearanceOption.DARK.storageValue)
        authRepository.currentUserProfile = AuthUserProfile(
            displayName = "Ahmed",
            email = "ahmed@example.com"
        )

        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertFalse(isLoading)
            assertFalse(notificationsEnabled)
            assertEquals(7, reminderHour)
            assertEquals(30, reminderMinute)
            assertEquals(SettingsLanguageOption.ENGLISH, selectedLanguage)
            assertEquals(SettingsAppearanceOption.DARK, selectedAppearance)
            assertTrue(account.isSignedIn)
            assertEquals("Ahmed", account.displayName)
            assertEquals("ahmed@example.com", account.email)
        }
    }

    @Test
    fun `notifications toggle persists and syncs reminders`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.NotificationsToggled(false))
        advanceUntilIdle()

        assertFalse(viewModel.state.value.notificationsEnabled)
        assertEquals(1, scheduler.syncCalls)
    }

    @Test
    fun `language selection updates persisted state without syncing reminders`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.LanguageSelected(SettingsLanguageOption.ENGLISH))
        advanceUntilIdle()

        assertEquals(SettingsLanguageOption.ENGLISH, viewModel.state.value.selectedLanguage)
        assertEquals(0, scheduler.syncCalls)
    }

    @Test
    fun `appearance selection updates persisted state without syncing reminders`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.AppearanceSelected(SettingsAppearanceOption.DARK))
        advanceUntilIdle()

        assertEquals(SettingsAppearanceOption.DARK, viewModel.state.value.selectedAppearance)
        assertEquals(0, scheduler.syncCalls)
    }

    @Test
    fun `reminder time selection updates state and syncs reminders`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.ReminderTimeSelected(11, 45))
        advanceUntilIdle()

        assertEquals("11:45", viewModel.state.value.reminderTimeText)
        assertEquals(1, scheduler.syncCalls)
    }

    @Test
    fun `clear favorites confirmation clears data and closes dialog`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.ClearFavoritesClicked)
        assertTrue(viewModel.state.value.showClearFavoritesDialog)

        viewModel.onIntent(SettingsIntent.ConfirmClearFavorites)
        advanceUntilIdle()

        assertEquals(1, preferences.clearFavoritesCalls)
        assertFalse(viewModel.state.value.showClearFavoritesDialog)
    }

    @Test
    fun `privacy and about dialogs open and dismiss independently`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.PrivacyPolicyClicked)
        assertTrue(viewModel.state.value.showPrivacyDialog)
        assertFalse(viewModel.state.value.showAboutDialog)

        viewModel.onIntent(SettingsIntent.PrivacyPolicyDismissed)
        assertFalse(viewModel.state.value.showPrivacyDialog)

        viewModel.onIntent(SettingsIntent.AboutAppClicked)
        assertTrue(viewModel.state.value.showAboutDialog)

        viewModel.onIntent(SettingsIntent.AboutAppDismissed)
        assertFalse(viewModel.state.value.showAboutDialog)
    }

    @Test
    fun `reset dialog dismiss keeps data untouched`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()

        viewModel.onIntent(SettingsIntent.ResetAppClicked)
        assertTrue(viewModel.state.value.showResetAppDialog)

        viewModel.onIntent(SettingsIntent.ResetAppDismissed)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.showResetAppDialog)
        assertEquals(0, preferences.resetAllCalls)
        assertEquals(0, authRepository.signOutCalls)
    }

    @Test
    fun `reset app signs out clears preferences and emits reset navigation`() = runTest {
        authRepository.currentUserProfile = AuthUserProfile("Ahmed", "ahmed@example.com")
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()
        val navigation = async { viewModel.navigationEvents.first() }

        viewModel.onIntent(SettingsIntent.ResetAppClicked)
        assertTrue(viewModel.state.value.showResetAppDialog)

        viewModel.onIntent(SettingsIntent.ConfirmResetApp)
        advanceUntilIdle()

        assertEquals(1, preferences.resetAllCalls)
        assertEquals(1, authRepository.signOutCalls)
        assertEquals(1, scheduler.syncCalls)
        assertEquals(SettingsNavigationEvent.ResetApp, navigation.await())
        assertFalse(viewModel.state.value.account.isSignedIn)
    }

    @Test
    fun `replay onboarding emits navigation event`() = runTest {
        val viewModel = SettingsViewModel(preferences, authRepository, scheduler)
        advanceUntilIdle()
        val navigation = async { viewModel.navigationEvents.first() }

        viewModel.onIntent(SettingsIntent.ReplayOnboardingClicked)
        advanceUntilIdle()

        assertEquals(SettingsNavigationEvent.ReplayOnboarding, navigation.await())
    }
}
