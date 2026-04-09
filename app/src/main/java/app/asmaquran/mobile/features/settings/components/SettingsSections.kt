package app.asmaquran.mobile.features.settings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.ManageAccounts
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.asmaquran.mobile.BuildConfig
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.theme.PrimaryGreen
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily
import app.asmaquran.mobile.features.settings.SettingsAccountUiModel
import app.asmaquran.mobile.features.settings.SettingsAppearanceOption
import app.asmaquran.mobile.features.settings.SettingsLanguageOption

@Composable
fun AccountSettingsSection(
    account: SettingsAccountUiModel,
    onSignInClick: () -> Unit
) {
    SettingsSectionCard(
        title = stringResource(R.string.settings_account_section),
        icon = Icons.Rounded.ManageAccounts
    ) {
        if (account.isSignedIn) {
            AccountSignedInRow(
                displayName = account.displayName.orEmpty(),
                email = account.email
            )
        } else {
            AccountGuestRow()
            SettingsDivider()
            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.settings_sign_in_google),
                    fontFamily = QuranFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun NotificationsSettingsSection(
    notificationsEnabled: Boolean,
    reminderTimeText: String,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onReminderTimeClick: () -> Unit
) {
    SettingsSectionCard(
        title = stringResource(R.string.settings_notifications_section),
        icon = Icons.Rounded.NotificationsNone
    ) {
        ToggleSettingRow(
            title = stringResource(R.string.settings_daily_name_reminder_title),
            subtitle = stringResource(R.string.settings_daily_name_reminder_subtitle),
            checked = notificationsEnabled,
            onCheckedChange = onNotificationsEnabledChange
        )
        SettingsDivider()
        ActionSettingRow(
            title = stringResource(R.string.settings_reminder_time_title),
            subtitle = reminderTimeText,
            leadingIcon = Icons.Rounded.Schedule,
            enabled = notificationsEnabled,
            onClick = onReminderTimeClick
        )
    }
}

@Composable
fun LanguageSettingsSection(
    selectedLanguage: SettingsLanguageOption,
    onLanguageSelected: (SettingsLanguageOption) -> Unit
) {
    SettingsSectionCard(
        title = stringResource(R.string.settings_language_section),
        icon = Icons.Rounded.Language
    ) {
        RadioSettingRow(
            title = stringResource(R.string.settings_language_arabic_title),
            subtitle = stringResource(R.string.settings_language_arabic_subtitle),
            selected = selectedLanguage == SettingsLanguageOption.ARABIC,
            onClick = { onLanguageSelected(SettingsLanguageOption.ARABIC) }
        )
        SettingsDivider()
        RadioSettingRow(
            title = stringResource(R.string.settings_language_english_title),
            subtitle = stringResource(R.string.settings_language_english_subtitle),
            selected = selectedLanguage == SettingsLanguageOption.ENGLISH,
            onClick = { onLanguageSelected(SettingsLanguageOption.ENGLISH) }
        )
    }
}

@Composable
fun AppearanceSettingsSection(
    selectedAppearance: SettingsAppearanceOption,
    onAppearanceSelected: (SettingsAppearanceOption) -> Unit
) {
    SettingsSectionCard(
        title = stringResource(R.string.settings_appearance_section),
        icon = Icons.Rounded.Palette
    ) {
        RadioSettingRow(
            title = stringResource(R.string.settings_appearance_light),
            selected = selectedAppearance == SettingsAppearanceOption.LIGHT,
            onClick = { onAppearanceSelected(SettingsAppearanceOption.LIGHT) }
        )
        SettingsDivider()
        RadioSettingRow(
            title = stringResource(R.string.settings_appearance_dark),
            selected = selectedAppearance == SettingsAppearanceOption.DARK,
            onClick = { onAppearanceSelected(SettingsAppearanceOption.DARK) }
        )
        SettingsDivider()
        RadioSettingRow(
            title = stringResource(R.string.settings_appearance_system),
            selected = selectedAppearance == SettingsAppearanceOption.SYSTEM,
            onClick = { onAppearanceSelected(SettingsAppearanceOption.SYSTEM) }
        )
    }
}

@Composable
fun GeneralSettingsSection(
    onReplayOnboardingClick: () -> Unit,
    onShareAppClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onAboutAppClick: () -> Unit
) {
    SettingsSectionCard(
        title = stringResource(R.string.settings_general_section),
        icon = Icons.Rounded.Settings
    ) {
        ActionSettingRow(
            title = stringResource(R.string.settings_replay_onboarding),
            leadingIcon = Icons.AutoMirrored.Rounded.MenuBook,
            onClick = onReplayOnboardingClick
        )
        SettingsDivider()
        ActionSettingRow(
            title = stringResource(R.string.settings_share_app),
            leadingIcon = Icons.Rounded.Share,
            onClick = onShareAppClick
        )
        SettingsDivider()
        ActionSettingRow(
            title = stringResource(R.string.settings_rate_app),
            leadingIcon = Icons.Rounded.StarOutline,
            onClick = onRateAppClick
        )
        SettingsDivider()
        ActionSettingRow(
            title = stringResource(R.string.settings_privacy_policy),
            leadingIcon = Icons.Rounded.Info,
            onClick = onPrivacyPolicyClick
        )
        SettingsDivider()
        ActionSettingRow(
            title = stringResource(R.string.settings_about_app),
            leadingIcon = Icons.Rounded.Info,
            onClick = onAboutAppClick
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.settings_version, BuildConfig.VERSION_NAME),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f),
                fontFamily = QuranFontFamily,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun DataSettingsSection(
    onClearFavoritesClick: () -> Unit,
    onResetAppClick: () -> Unit
) {
    SettingsSectionCard(
        title = stringResource(R.string.settings_data_section),
        icon = Icons.Rounded.DeleteOutline,
        accentColor = Color(0xFFE64A4A),
        borderColor = Color(0xFFFFE2E2)
    ) {
        DangerSettingRow(
            title = stringResource(R.string.settings_clear_favorites),
            leadingIcon = Icons.Rounded.DeleteOutline,
            onClick = onClearFavoritesClick
        )
        SettingsDivider(color = Color(0xFFFFECEC))
        DangerSettingRow(
            title = stringResource(R.string.settings_reset_app),
            subtitle = stringResource(R.string.settings_reset_app_subtitle),
            leadingIcon = Icons.Rounded.RestartAlt,
            onClick = onResetAppClick
        )
    }
}
