package app.asmaquran.mobile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import app.asmaquran.mobile.core.ui.theme.AllahNamesQuranTheme
import app.asmaquran.mobile.data.preferences.AppPreferences
import app.asmaquran.mobile.navigation.AppNavHost
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
            AllahNamesQuranTheme {
                AppNavHost(
                    dailyNameIdFromNotification = intent
                        ?.getIntExtra(EXTRA_DAILY_NAME_ID, -1)
                        ?.takeIf { it > 0 }
                )
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
