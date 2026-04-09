package app.asmaquran.mobile.features.settings

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.asmaquran.mobile.R
import app.asmaquran.mobile.data.auth.AuthProviderType
import app.asmaquran.mobile.data.auth.AuthRepository
import app.asmaquran.mobile.data.auth.AuthSignInResult
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
        refreshAccount()
    }

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.SignInClicked -> {
                state.value = state.value.copy(
                    showSignInDialog = true,
                    authErrorMessageRes = null
                )
            }

            SettingsIntent.SignInDialogDismissed -> {
                if (state.value.isAuthLoading) return
                state.value = state.value.copy(
                    showSignInDialog = false,
                    authErrorMessageRes = null
                )
            }

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

    fun signInWithGoogle(activity: Activity) {
        if (state.value.isAuthLoading) return

        viewModelScope.launch {
            state.update { current ->
                current.copy(
                    loadingProvider = AuthProviderType.GOOGLE,
                    authErrorMessageRes = null
                )
            }

            when (authRepository.signInWithGoogle(activity)) {
                AuthSignInResult.Success -> {
                    updateAccountState()
                    clearAuthState(closeDialog = true)
                }

                AuthSignInResult.Started -> {
                    clearAuthState(closeDialog = true)
                }

                AuthSignInResult.Cancelled -> {
                    setAuthError(R.string.sign_in_error_cancelled)
                }

                AuthSignInResult.NotConfigured -> {
                    setAuthError(R.string.sign_in_error_not_configured)
                }

                AuthSignInResult.NoToken -> {
                    setAuthError(R.string.sign_in_error_google_token)
                }

                is AuthSignInResult.Failure -> {
                    setAuthError(R.string.sign_in_error_generic)
                }
            }
        }
    }

    fun signInWithGithub() {
        if (state.value.isAuthLoading) return

        viewModelScope.launch {
            state.update { current ->
                current.copy(
                    loadingProvider = AuthProviderType.GITHUB,
                    authErrorMessageRes = null
                )
            }

            when (authRepository.signInWithGithub()) {
                AuthSignInResult.Success -> {
                    updateAccountState()
                    clearAuthState(closeDialog = true)
                }

                AuthSignInResult.Started -> {
                    clearAuthState(closeDialog = true)
                }

                AuthSignInResult.Cancelled -> {
                    setAuthError(R.string.sign_in_error_cancelled)
                }

                AuthSignInResult.NotConfigured -> {
                    setAuthError(R.string.sign_in_error_not_configured)
                }

                AuthSignInResult.NoToken -> {
                    setAuthError(R.string.sign_in_error_generic)
                }

                is AuthSignInResult.Failure -> {
                    setAuthError(R.string.sign_in_error_generic)
                }
            }
        }
    }

    fun refreshAccount() {
        viewModelScope.launch {
            updateAccountState()
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

    private suspend fun updateAccountState() {
        val profile = authRepository.getCurrentUserProfile()
        state.update { current ->
            current.copy(
                account = SettingsAccountUiModel(
                    isSignedIn = profile != null,
                    displayName = profile?.displayName,
                    email = profile?.email,
                    provider = profile?.provider
                ),
                showSignInDialog = if (profile != null) false else current.showSignInDialog,
                loadingProvider = if (profile != null) null else current.loadingProvider,
                authErrorMessageRes = if (profile != null) null else current.authErrorMessageRes
            )
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
                    showSignInDialog = false,
                    loadingProvider = null,
                    authErrorMessageRes = null,
                    account = SettingsAccountUiModel()
                )
            }
            _navigationEvents.emit(SettingsNavigationEvent.ResetApp)
        }
    }

    private fun clearAuthState(closeDialog: Boolean) {
        state.update { current ->
            current.copy(
                showSignInDialog = if (closeDialog) false else current.showSignInDialog,
                loadingProvider = null,
                authErrorMessageRes = null
            )
        }
    }

    private fun setAuthError(@androidx.annotation.StringRes messageRes: Int) {
        state.update { current ->
            current.copy(
                loadingProvider = null,
                authErrorMessageRes = messageRes
            )
        }
    }
}
