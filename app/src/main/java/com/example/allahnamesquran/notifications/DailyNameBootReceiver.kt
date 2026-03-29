package com.example.allahnamesquran.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DailyNameBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        AndroidDailyNameReminderScheduler(context).scheduleDailyReminder()
    }
}
