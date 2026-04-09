package app.asmaquran.mobile.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.preferences.SettingsPreferencesStore
import app.asmaquran.mobile.notifications.DailyNameReminderScheduler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsPreferences: SettingsPreferencesStore,
    private val authRepository: AuthRepository,
    private val dailyNameReminderScheduler: DailyNameReminderScheduler
) : ViewModel() {

    val state: StateFlow<SettingsUiState>
    field = MutableStateFlow(SettingsUiState())

    private val _navigationEvents = MutableSharedFlow<SettingsNavigationEvent>()
    val navigationEvents: SharedFlow<SettingsNavigationEvent> = _navigationEvents

    init {
        observeSettings()
        loadAccount()
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.AboutAppClicked -> {
                state.value = state.value.copy(showAboutDialog = true)
            }

            SettingsIntent.AboutAppDismissed -> {
                state.value = state.value.copy(showAboutDialog = false)
            }

            SettingsIntent.PrivacyPolicyClicked -> {
                state.value = state.value.copy(showPrivacyDialog = true)
            }

            SettingsIntent.PrivacyPolicyDismissed -> {
                state.value = state.value.copy(showPrivacyDialog = false)
            }

            SettingsIntent.ClearFavoritesClicked -> {
                state.value = state.value.copy(showClearFavoritesDialog = true)
            }

            SettingsIntent.ClearFavoritesDismissed -> {
                state.value = state.value.copy(showClearFavoritesDialog = false)
            }

            SettingsIntent.ResetAppClicked -> {
                state.value = state.value.copy(showResetAppDialog = true)
            }

            SettingsIntent.ResetAppDismissed -> {
                state.value = state.value.copy(showResetAppDialog = false)
            }

            SettingsIntent.ReplayOnboardingClicked -> {
                viewModelScope.launch {
                    _navigationEvents.emit(SettingsNavigationEvent.ReplayOnboarding)
                }
            }

            is SettingsIntent.NotificationsToggled -> {
                updateNotifications(intent.enabled)
            }

            is SettingsIntent.ReminderTimeSelected -> {
                updateReminderTime(
                    hour = intent.hour,
                    minute = intent.minute
                )
            }

            is SettingsIntent.LanguageSelected -> {
                updateLanguage(intent.option)
            }

            is SettingsIntent.AppearanceSelected -> {
                updateAppearance(intent.option)
            }

            SettingsIntent.ConfirmClearFavorites -> {
                clearFavorites()
            }

            SettingsIntent.ConfirmResetApp -> {
                resetApp()
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            combine(
                settingsPreferences.dailyReminderEnabled,
                settingsPreferences.dailyReminderHour,
                settingsPreferences.dailyReminderMinute,
                settingsPreferences.selectedLanguageTag,
                settingsPreferences.selectedAppearanceMode
            ) { notificationsEnabled, reminderHour, reminderMinute, languageTag, appearanceMode ->
                SettingsUiState(
                    notificationsEnabled = notificationsEnabled,
                    reminderHour = reminderHour,
                    reminderMinute = reminderMinute,
                    selectedLanguage = SettingsLanguageOption.fromStorage(languageTag),
                    selectedAppearance = SettingsAppearanceOption.fromStorage(appearanceMode)
                )
            }.collect { preferencesState ->
                state.update { current ->
                    current.copy(
                        isLoading = false,
                        notificationsEnabled = preferencesState.notificationsEnabled,
                        reminderHour = preferencesState.reminderHour,
                        reminderMinute = preferencesState.reminderMinute,
                        selectedLanguage = preferencesState.selectedLanguage,
                        selectedAppearance = preferencesState.selectedAppearance
                    )
                }
            }
        }
    }

    private fun loadAccount() {
        viewModelScope.launch {
            val profile = authRepository.getCurrentUserProfile()
            state.update { current ->
                current.copy(
                    account = SettingsAccountUiModel(
                        isSignedIn = profile != null,
                        displayName = profile?.displayName,
                        email = profile?.email
                    )
                )
            }
        }
    }

    private fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsPreferences.setDailyReminderEnabled(enabled)
            dailyNameReminderScheduler.syncDailyReminder()
        }
    }

    private fun updateReminderTime(hour: Int, minute: Int) {
        viewModelScope.launch {
            settingsPreferences.setDailyReminderTime(hour, minute)
            dailyNameReminderScheduler.syncDailyReminder()
        }
    }

    private fun updateLanguage(option: SettingsLanguageOption) {
        viewModelScope.launch {
            settingsPreferences.setSelectedLanguageTag(option.storageValue)
        }
    }

    private fun updateAppearance(option: SettingsAppearanceOption) {
        viewModelScope.launch {
            settingsPreferences.setSelectedAppearanceMode(option.storageValue)
        }
    }

    private fun clearFavorites() {
        viewModelScope.launch {
            settingsPreferences.clearFavoriteNameIds()
            state.update { current ->
                current.copy(showClearFavoritesDialog = false)
            }
        }
    }

    private fun resetApp() {
        viewModelScope.launch {
            runCatching { authRepository.signOut() }
            settingsPreferences.resetAll()
            dailyNameReminderScheduler.syncDailyReminder()
            state.update { current ->
                current.copy(
                    showResetAppDialog = false,
                    showAboutDialog = false,
                    showPrivacyDialog = false,
                    account = SettingsAccountUiModel()
                )
            }
            _navigationEvents.emit(SettingsNavigationEvent.ResetApp)
        }
    }
}
