package com.example.allahnamesquran.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

class AndroidDailyNameReminderScheduler(
    private val context: Context
) : DailyNameReminderScheduler {

    override fun scheduleDailyReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            DailyNameNotificationReceiver.REQUEST_CODE,
            Intent(context, DailyNameNotificationReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            nextTriggerTimeMillis(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun nextTriggerTimeMillis(): Long {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (next.before(now)) {
            next.add(Calendar.DAY_OF_YEAR, 1)
        }

        return next.timeInMillis
    }
}
