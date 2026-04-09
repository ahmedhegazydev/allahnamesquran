package app.asmaquran.mobile.testutil

import app.asmaquran.mobile.notifications.DailyNameReminderScheduler

class FakeDailyNameReminderScheduler : DailyNameReminderScheduler {

    var syncCalls = 0

    override suspend fun syncDailyReminder() {
        syncCalls++
    }
}
