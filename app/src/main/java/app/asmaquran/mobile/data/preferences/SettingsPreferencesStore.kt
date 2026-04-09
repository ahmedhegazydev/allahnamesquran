package app.asmaquran.mobile.data.preferences

import kotlinx.coroutines.flow.Flow

interface SettingsPreferencesStore {
    val dailyReminderEnabled: Flow<Boolean>
    val dailyReminderHour: Flow<Int>
    val dailyReminderMinute: Flow<Int>
    val selectedLanguageTag: Flow<String>
    val selectedAppearanceMode: Flow<String>

    suspend fun setDailyReminderEnabled(value: Boolean)
    suspend fun setDailyReminderTime(hour: Int, minute: Int)
    suspend fun setSelectedLanguageTag(value: String)
    suspend fun setSelectedAppearanceMode(value: String)
    suspend fun clearFavoriteNameIds()
    suspend fun resetAll()
}
