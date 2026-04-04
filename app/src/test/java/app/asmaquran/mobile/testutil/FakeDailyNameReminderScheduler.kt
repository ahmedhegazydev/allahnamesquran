package app.asmaquran.mobile.testutil

import app.asmaquran.mobile.notifications.DailyNameReminderScheduler

class FakeDailyNameReminderScheduler : DailyNameReminderScheduler {

    var scheduleCalls = 0

    override fun scheduleDailyReminder() {
        scheduleCalls++
    }
}
