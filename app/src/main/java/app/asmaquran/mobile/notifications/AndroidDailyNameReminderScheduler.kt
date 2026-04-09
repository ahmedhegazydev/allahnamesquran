package app.asmaquran.mobile.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import app.asmaquran.mobile.data.preferences.AppPreferences
import java.util.Calendar
import kotlinx.coroutines.flow.first

class AndroidDailyNameReminderScheduler(
    private val context: Context
) : DailyNameReminderScheduler {

    override suspend fun syncDailyReminder() {
        val appPreferences = AppPreferences(context)
        val isEnabled = appPreferences.dailyReminderEnabled.first()
        val hour = appPreferences.dailyReminderHour.first()
        val minute = appPreferences.dailyReminderMinute.first()

        if (!isEnabled) {
            cancelDailyReminder()
            return
        }

        scheduleDailyReminder(
            hour = hour,
            minute = minute
        )
    }

    private fun scheduleDailyReminder(
        hour: Int,
        minute: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = reminderPendingIntent()

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            nextTriggerTimeMillis(hour = hour, minute = minute),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelDailyReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(reminderPendingIntent())
    }

    private fun reminderPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            DailyNameNotificationReceiver.REQUEST_CODE,
            Intent(context, DailyNameNotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun nextTriggerTimeMillis(
        hour: Int,
        minute: Int
    ): Long {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (next.before(now)) {
            next.add(Calendar.DAY_OF_YEAR, 1)
        }

        return next.timeInMillis
    }
}
