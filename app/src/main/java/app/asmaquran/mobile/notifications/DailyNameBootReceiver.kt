package app.asmaquran.mobile.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.runBlocking

class DailyNameBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        runBlocking {
            AndroidDailyNameReminderScheduler(context).syncDailyReminder()
        }
    }
}
