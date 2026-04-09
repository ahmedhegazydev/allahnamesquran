package app.asmaquran.mobile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import app.asmaquran.mobile.core.ui.theme.AllahNamesQuranTheme
import app.asmaquran.mobile.data.preferences.AppPreferences
import app.asmaquran.mobile.data.remote.supabase.SupabaseProvider
import app.asmaquran.mobile.features.settings.SettingsAppearanceOption
import app.asmaquran.mobile.features.settings.SettingsLanguageOption
import app.asmaquran.mobile.navigation.AppNavHost
import io.github.jan.supabase.auth.handleDeeplinks
import java.util.Locale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val appPreferences by lazy {
        AppPreferences(applicationContext)
    }
    private val supabaseProvider: SupabaseProvider by inject()

    private val notificationsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        handleAuthDeepLink(intent)
        maybeRequestNotificationsPermission()

        setContent {
            val configuration = LocalConfiguration.current
            val context = LocalContext.current
            val selectedLanguageTag by appPreferences.selectedLanguageTag.collectAsStateWithLifecycle(
                initialValue = SettingsLanguageOption.ARABIC.storageValue
            )
            val selectedAppearanceMode by appPreferences.selectedAppearanceMode.collectAsStateWithLifecycle(
                initialValue = SettingsAppearanceOption.SYSTEM.storageValue
            )

            val selectedLanguage = remember(selectedLanguageTag) {
                SettingsLanguageOption.fromStorage(selectedLanguageTag)
            }
            val selectedAppearance = remember(selectedAppearanceMode) {
                SettingsAppearanceOption.fromStorage(selectedAppearanceMode)
            }
            val localizedContext = remember(configuration, context, selectedLanguage) {
                context.localize(selectedLanguage.localeTag)
            }
            val localizedLocale = remember(selectedLanguage) {
                Locale.forLanguageTag(selectedLanguage.localeTag)
            }
            val layoutDirection = remember(localizedLocale) {
                if (TextUtilsCompat.getLayoutDirectionFromLocale(localizedLocale) == View.LAYOUT_DIRECTION_RTL) {
                    LayoutDirection.Rtl
                } else {
                    LayoutDirection.Ltr
                }
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalLayoutDirection provides layoutDirection
            ) {
                AllahNamesQuranTheme(appearanceOption = selectedAppearance) {
                    AppNavHost(
                        dailyNameIdFromNotification = intent
                            ?.getIntExtra(EXTRA_DAILY_NAME_ID, -1)
                            ?.takeIf { it > 0 }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleAuthDeepLink(intent)
    }

    private fun maybeRequestNotificationsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        lifecycleScope.launch {
            if (appPreferences.notificationsPermissionRequested.first()) return@launch

            appPreferences.setNotificationsPermissionRequested(true)
            notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun handleAuthDeepLink(intent: Intent?) {
        val authIntent = intent ?: return
        val client = supabaseProvider.client ?: return

        client.handleDeeplinks(authIntent) {
            lifecycleScope.launch {
                appPreferences.setAuthPromptCompleted(true)
            }
        }
    }

    companion object {
        const val EXTRA_DAILY_NAME_ID = "extra_daily_name_id"
    }
}

private fun Context.localize(languageTag: String): Context {
    val locale = Locale.forLanguageTag(languageTag)
    Locale.setDefault(locale)

    val configuration = android.content.res.Configuration(resources.configuration)
    configuration.setLocale(locale)
    val localizedContext = createConfigurationContext(configuration)

    return object : ContextWrapper(this) {
        override fun getResources() = localizedContext.resources

        override fun getAssets() = localizedContext.assets

        override fun getTheme() = this@localize.theme
    }
}
