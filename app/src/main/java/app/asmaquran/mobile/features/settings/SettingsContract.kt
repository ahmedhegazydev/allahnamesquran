package app.asmaquran.mobile.features.settings

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import app.asmaquran.mobile.data.auth.AuthProviderType

@Immutable
data class SettingsAccountUiModel(
    val isSignedIn: Boolean = false,
    val displayName: String? = null,
    val email: String? = null,
    val provider: AuthProviderType? = null
)

@Immutable
data class SettingsUiState(
    val isLoading: Boolean = true,
    val account: SettingsAccountUiModel = SettingsAccountUiModel(),
    val showSignInDialog: Boolean = false,
    val loadingProvider: AuthProviderType? = null,
    @param:StringRes val authErrorMessageRes: Int? = null,
    val notificationsEnabled: Boolean = true,
    val reminderHour: Int = 9,
    val reminderMinute: Int = 0,
    val selectedLanguage: SettingsLanguageOption = SettingsLanguageOption.ARABIC,
    val selectedAppearance: SettingsAppearanceOption = SettingsAppearanceOption.SYSTEM,
    val showPrivacyDialog: Boolean = false,
    val showAboutDialog: Boolean = false,
    val showClearFavoritesDialog: Boolean = false,
    val showResetAppDialog: Boolean = false
) {
    val reminderTimeText: String
        get() = String.format("%02d:%02d", reminderHour, reminderMinute)

    val isAuthLoading: Boolean
        get() = loadingProvider != null
}

sealed interface SettingsIntent {
    data object SignInClicked : SettingsIntent
    data object SignInDialogDismissed : SettingsIntent
    data object PrivacyPolicyClicked : SettingsIntent
    data object PrivacyPolicyDismissed : SettingsIntent
    data object AboutAppClicked : SettingsIntent
    data object AboutAppDismissed : SettingsIntent
    data object ReplayOnboardingClicked : SettingsIntent
    data class NotificationsToggled(val enabled: Boolean) : SettingsIntent
    data class ReminderTimeSelected(val hour: Int, val minute: Int) : SettingsIntent
    data class LanguageSelected(val option: SettingsLanguageOption) : SettingsIntent
    data class AppearanceSelected(val option: SettingsAppearanceOption) : SettingsIntent
    data object ClearFavoritesClicked : SettingsIntent
    data object ClearFavoritesDismissed : SettingsIntent
    data object ConfirmClearFavorites : SettingsIntent
    data object ResetAppClicked : SettingsIntent
    data object ResetAppDismissed : SettingsIntent
    data object ConfirmResetApp : SettingsIntent
}

sealed interface SettingsNavigationEvent {
    data object ReplayOnboarding : SettingsNavigationEvent
    data object ResetApp : SettingsNavigationEvent
}
