package app.asmaquran.mobile.features.settings

import android.app.Activity
import android.app.TimePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.asmaquran.mobile.BuildConfig
import app.asmaquran.mobile.R
import app.asmaquran.mobile.core.ui.components.GithubProviderMark
import app.asmaquran.mobile.core.ui.components.GoogleProviderMark
import app.asmaquran.mobile.core.ui.preview.AppScreenPreviews
import app.asmaquran.mobile.core.ui.preview.PreviewSurface
import app.asmaquran.mobile.core.ui.theme.AppBackground
import app.asmaquran.mobile.core.ui.theme.QuranFontFamily
import app.asmaquran.mobile.features.settings.components.AppearanceSettingsSection
import app.asmaquran.mobile.features.settings.components.AccountSettingsSection
import app.asmaquran.mobile.features.settings.components.DataSettingsSection
import app.asmaquran.mobile.features.settings.components.GeneralSettingsSection
import app.asmaquran.mobile.features.settings.components.LanguageSettingsSection
import app.asmaquran.mobile.features.settings.components.NotificationsSettingsSection
import app.asmaquran.mobile.features.settings.components.SettingsHeader
import app.asmaquran.mobile.data.auth.AuthProviderType
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onReplayOnboarding: () -> Unit,
    onResetApp: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context.findActivity()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                SettingsNavigationEvent.ReplayOnboarding -> onReplayOnboarding()
                SettingsNavigationEvent.ResetApp -> onResetApp()
            }
        }
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshAccount()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SettingsScreenContent(
        state = state,
        onBackClick = onBackClick,
        onOpenSignInDialog = {
            viewModel.onIntent(SettingsIntent.SignInClicked)
        },
        onDismissSignInDialog = {
            viewModel.onIntent(SettingsIntent.SignInDialogDismissed)
        },
        onGoogleSignInClick = {
            activity?.let(viewModel::signInWithGoogle)
        },
        onGithubSignInClick = viewModel::signInWithGithub,
        onNotificationsEnabledChange = {
            viewModel.onIntent(SettingsIntent.NotificationsToggled(it))
        },
        onReminderTimeClick = {
            showReminderTimePicker(
                context = context,
                hour = state.reminderHour,
                minute = state.reminderMinute
            ) { hour, minute ->
                viewModel.onIntent(SettingsIntent.ReminderTimeSelected(hour, minute))
            }
        },
        onLanguageSelected = {
            viewModel.onIntent(SettingsIntent.LanguageSelected(it))
        },
        onAppearanceSelected = {
            viewModel.onIntent(SettingsIntent.AppearanceSelected(it))
        },
        onReplayOnboardingClick = {
            viewModel.onIntent(SettingsIntent.ReplayOnboardingClicked)
        },
        onShareAppClick = {
            shareApp(context)
        },
        onRateAppClick = {
            rateApp(context)
        },
        onPrivacyPolicyClick = {
            viewModel.onIntent(SettingsIntent.PrivacyPolicyClicked)
        },
        onAboutAppClick = {
            viewModel.onIntent(SettingsIntent.AboutAppClicked)
        },
        onClearFavoritesClick = {
            viewModel.onIntent(SettingsIntent.ClearFavoritesClicked)
        },
        onResetAppClick = {
            viewModel.onIntent(SettingsIntent.ResetAppClicked)
        },
        onDismissPrivacyDialog = {
            viewModel.onIntent(SettingsIntent.PrivacyPolicyDismissed)
        },
        onDismissAboutDialog = {
            viewModel.onIntent(SettingsIntent.AboutAppDismissed)
        },
        onDismissClearFavoritesDialog = {
            viewModel.onIntent(SettingsIntent.ClearFavoritesDismissed)
        },
        onConfirmClearFavorites = {
            viewModel.onIntent(SettingsIntent.ConfirmClearFavorites)
        },
        onDismissResetAppDialog = {
            viewModel.onIntent(SettingsIntent.ResetAppDismissed)
        },
        onConfirmResetApp = {
            viewModel.onIntent(SettingsIntent.ConfirmResetApp)
        }
    )
}

@Composable
private fun SettingsScreenContent(
    state: SettingsUiState,
    onBackClick: () -> Unit,
    onOpenSignInDialog: () -> Unit,
    onDismissSignInDialog: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    onGithubSignInClick: () -> Unit,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onReminderTimeClick: () -> Unit,
    onLanguageSelected: (SettingsLanguageOption) -> Unit,
    onAppearanceSelected: (SettingsAppearanceOption) -> Unit,
    onReplayOnboardingClick: () -> Unit,
    onShareAppClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onAboutAppClick: () -> Unit,
    onClearFavoritesClick: () -> Unit,
    onResetAppClick: () -> Unit,
    onDismissPrivacyDialog: () -> Unit,
    onDismissAboutDialog: () -> Unit,
    onDismissClearFavoritesDialog: () -> Unit,
    onConfirmClearFavorites: () -> Unit,
    onDismissResetAppDialog: () -> Unit,
    onConfirmResetApp: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            SettingsHeader(onBackClick = onBackClick)
        }

        item {
            AccountSettingsSection(
                account = state.account,
                onSignInClick = onOpenSignInDialog
            )
        }

        item {
            NotificationsSettingsSection(
                notificationsEnabled = state.notificationsEnabled,
                reminderTimeText = state.reminderTimeText,
                onNotificationsEnabledChange = onNotificationsEnabledChange,
                onReminderTimeClick = onReminderTimeClick
            )
        }

        item {
            LanguageSettingsSection(
                selectedLanguage = state.selectedLanguage,
                onLanguageSelected = onLanguageSelected
            )
        }

        item {
            AppearanceSettingsSection(
                selectedAppearance = state.selectedAppearance,
                onAppearanceSelected = onAppearanceSelected
            )
        }

        item {
            GeneralSettingsSection(
                onReplayOnboardingClick = onReplayOnboardingClick,
                onShareAppClick = onShareAppClick,
                onRateAppClick = onRateAppClick,
                onPrivacyPolicyClick = onPrivacyPolicyClick,
                onAboutAppClick = onAboutAppClick
            )
        }

        item {
            DataSettingsSection(
                onClearFavoritesClick = onClearFavoritesClick,
                onResetAppClick = onResetAppClick
            )
        }
    }

    if (state.showPrivacyDialog) {
        SettingsMessageDialog(
            title = stringResource(R.string.settings_privacy_policy),
            message = stringResource(R.string.settings_privacy_message),
            onDismiss = onDismissPrivacyDialog
        )
    }

    if (state.showAboutDialog) {
        SettingsMessageDialog(
            title = stringResource(R.string.settings_about_app),
            message = stringResource(
                R.string.settings_about_message,
                BuildConfig.VERSION_NAME
            ),
            onDismiss = onDismissAboutDialog
        )
    }

    if (state.showClearFavoritesDialog) {
        SettingsConfirmationDialog(
            title = stringResource(R.string.settings_clear_favorites_confirm_title),
            message = stringResource(R.string.settings_clear_favorites_confirm_message),
            confirmText = stringResource(R.string.settings_clear_favorites),
            onDismiss = onDismissClearFavoritesDialog,
            onConfirm = onConfirmClearFavorites
        )
    }

    if (state.showResetAppDialog) {
        SettingsConfirmationDialog(
            title = stringResource(R.string.settings_reset_confirm_title),
            message = stringResource(R.string.settings_reset_confirm_message),
            confirmText = stringResource(R.string.settings_reset_app),
            onDismiss = onDismissResetAppDialog,
            onConfirm = onConfirmResetApp,
            isDanger = true
        )
    }

    if (state.showSignInDialog) {
        SettingsSignInDialog(
            state = state,
            onDismiss = onDismissSignInDialog,
            onGoogleClick = onGoogleSignInClick,
            onGithubClick = onGithubSignInClick
        )
    }
}

@Composable
private fun SettingsSignInDialog(
    state: SettingsUiState,
    onDismiss: () -> Unit,
    onGoogleClick: () -> Unit,
    onGithubClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            if (!state.isAuthLoading) {
                onDismiss()
            }
        },
        title = {
            Text(
                text = stringResource(R.string.settings_sign_in_dialog_title),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = QuranFontFamily
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = stringResource(R.string.settings_sign_in_dialog_message),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = QuranFontFamily,
                    lineHeight = 24.sp
                )

                SettingsAuthButton(
                    text = stringResource(R.string.sign_in_google_cta),
                    loading = state.loadingProvider == AuthProviderType.GOOGLE,
                    enabled = !state.isAuthLoading,
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = onGoogleClick,
                    icon = {
                        GoogleProviderMark(modifier = Modifier.size(20.dp))
                    }
                )

                SettingsAuthButton(
                    text = stringResource(R.string.sign_in_github_cta),
                    loading = state.loadingProvider == AuthProviderType.GITHUB,
                    enabled = !state.isAuthLoading,
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    onClick = onGithubClick,
                    icon = {
                        GithubProviderMark(
                            modifier = Modifier.size(20.dp),
                            iconTint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )

                state.authErrorMessageRes?.let { messageRes ->
                    Text(
                        text = stringResource(messageRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = QuranFontFamily
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !state.isAuthLoading
            ) {
                Text(text = stringResource(R.string.settings_dialog_cancel))
            }
        }
    )
}

@Composable
private fun SettingsAuthButton(
    text: String,
    loading: Boolean,
    enabled: Boolean,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = contentColor
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = text,
                    fontFamily = QuranFontFamily
                )
                icon()
            }
        }
    }
}

@Composable
private fun SettingsMessageDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.settings_dialog_close))
            }
        }
    )
}

@Composable
private fun SettingsConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isDanger: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = if (isDanger) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.settings_dialog_cancel))
            }
        }
    )
}

private fun showReminderTimePicker(
    context: Context,
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {
    TimePickerDialog(
        context,
        { _, selectedHour, selectedMinute ->
            onTimeSelected(selectedHour, selectedMinute)
        },
        hour,
        minute,
        true
    ).show()
}

private fun shareApp(context: Context) {
    val shareText = context.getString(
        R.string.settings_share_app_message,
        context.packageName
    )
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, shareText)
    }

    context.startActivity(
        Intent.createChooser(
            shareIntent,
            context.getString(R.string.settings_share_app)
        )
    )
}

private fun rateApp(context: Context) {
    val packageName = context.packageName
    val marketIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("market://details?id=$packageName")
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    val webIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
    ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(marketIntent)
    } catch (_: ActivityNotFoundException) {
        context.startActivity(webIntent)
    } catch (_: Throwable) {
        Toast.makeText(
            context,
            context.getString(R.string.settings_rate_app_error),
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}

@AppScreenPreviews
@Composable
private fun SettingsScreenPreview() {
    PreviewSurface {
        SettingsScreenContent(
            state = SettingsUiState(
                isLoading = false,
                notificationsEnabled = true,
                reminderHour = 9,
                reminderMinute = 0,
                selectedLanguage = SettingsLanguageOption.ARABIC,
                selectedAppearance = SettingsAppearanceOption.SYSTEM,
                account = SettingsAccountUiModel()
            ),
            onBackClick = {},
            onOpenSignInDialog = {},
            onDismissSignInDialog = {},
            onGoogleSignInClick = {},
            onGithubSignInClick = {},
            onNotificationsEnabledChange = {},
            onReminderTimeClick = {},
            onLanguageSelected = {},
            onAppearanceSelected = {},
            onReplayOnboardingClick = {},
            onShareAppClick = {},
            onRateAppClick = {},
            onPrivacyPolicyClick = {},
            onAboutAppClick = {},
            onClearFavoritesClick = {},
            onResetAppClick = {},
            onDismissPrivacyDialog = {},
            onDismissAboutDialog = {},
            onDismissClearFavoritesDialog = {},
            onConfirmClearFavorites = {},
            onDismissResetAppDialog = {},
            onConfirmResetApp = {}
        )
    }
}
