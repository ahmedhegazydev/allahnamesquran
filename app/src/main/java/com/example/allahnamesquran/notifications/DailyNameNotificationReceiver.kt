package com.example.allahnamesquran.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.allahnamesquran.MainActivity
import com.example.allahnamesquran.R
import com.example.allahnamesquran.data.repository.QuranRepository
import com.example.allahnamesquran.features.home.DailyNameContentFormatter
import com.example.allahnamesquran.features.home.DailyNameFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DailyNameNotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val repository: QuranRepository by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val name = DailyNameFactory.getNameForDate(repository.getAllAllahNames()) ?: return@runCatching
                val ayah = repository.searchAyahsByAllahName(name.name).firstOrNull()

                createChannel(context)

                val openAppIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra(MainActivity.EXTRA_DAILY_NAME_ID, name.id)
                }

                val contentIntent = PendingIntent.getActivity(
                    context,
                    name.id,
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val body = ayah?.text ?: DailyNameContentFormatter.buildNotificationBody(name)
                val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_today)
                    .setContentTitle(context.getString(R.string.daily_name_notification_title, name.name))
                    .setContentText(body)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .build()

                val notificationManager =
                    ContextCompat.getSystemService(context, NotificationManager::class.java)
                notificationManager?.notify(NOTIFICATION_ID, notification)
            }

            pendingResult.finish()
        }
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.daily_name_notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.daily_name_notification_channel_description)
        }

        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "daily_name_channel"
        const val NOTIFICATION_ID = 5010
        const val REQUEST_CODE = 9001
    }
}
