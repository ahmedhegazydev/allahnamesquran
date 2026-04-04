package app.asmaquran.mobile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.lifecycle.lifecycleScope
import app.asmaquran.mobile.core.ui.theme.AllahNamesQuranTheme
import app.asmaquran.mobile.data.preferences.AppPreferences
import app.asmaquran.mobile.navigation.AppNavHost
import java.util.Locale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val notificationsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        maybeRequestNotificationsPermission()

        setContent {
            val configuration = LocalConfiguration.current
            val primaryLocale = remember(configuration) {
                configuration.primaryLocale()
            }
            val layoutDirection = remember(primaryLocale) {
                if (TextUtilsCompat.getLayoutDirectionFromLocale(primaryLocale) == View.LAYOUT_DIRECTION_RTL) {
                    LayoutDirection.Rtl
                } else {
                    LayoutDirection.Ltr
                }
            }

            CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
                AllahNamesQuranTheme {
                    AppNavHost(
                        dailyNameIdFromNotification = intent
                            ?.getIntExtra(EXTRA_DAILY_NAME_ID, -1)
                            ?.takeIf { it > 0 }
                    )
                }
            }
        }
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

        val appPreferences = AppPreferences(applicationContext)
        lifecycleScope.launch {
            if (appPreferences.notificationsPermissionRequested.first()) return@launch

            appPreferences.setNotificationsPermissionRequested(true)
            notificationsPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    companion object {
        const val EXTRA_DAILY_NAME_ID = "extra_daily_name_id"
    }
}

private fun android.content.res.Configuration.primaryLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        locales[0] ?: Locale.getDefault()
    } else {
        @Suppress("DEPRECATION")
        locale ?: Locale.getDefault()
    }
}
