package app.asmaquran.mobile.testutil

import app.asmaquran.mobile.data.preferences.SettingsPreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSettingsPreferencesStore : SettingsPreferencesStore {

    private val defaultLanguage = "ar"
    private val defaultAppearance = "system"

    private val dailyReminderEnabledState = MutableStateFlow(true)
    private val dailyReminderHourState = MutableStateFlow(9)
    private val dailyReminderMinuteState = MutableStateFlow(0)
    private val selectedLanguageTagState = MutableStateFlow(defaultLanguage)
    private val selectedAppearanceModeState = MutableStateFlow(defaultAppearance)

    var clearFavoritesCalls = 0
    var resetAllCalls = 0

    override val dailyReminderEnabled: Flow<Boolean> = dailyReminderEnabledState
    override val dailyReminderHour: Flow<Int> = dailyReminderHourState
    override val dailyReminderMinute: Flow<Int> = dailyReminderMinuteState
    override val selectedLanguageTag: Flow<String> = selectedLanguageTagState
    override val selectedAppearanceMode: Flow<String> = selectedAppearanceModeState

    override suspend fun setDailyReminderEnabled(value: Boolean) {
        dailyReminderEnabledState.value = value
    }

    override suspend fun setDailyReminderTime(hour: Int, minute: Int) {
        dailyReminderHourState.value = hour
        dailyReminderMinuteState.value = minute
    }

    override suspend fun setSelectedLanguageTag(value: String) {
        selectedLanguageTagState.value = value
    }

    override suspend fun setSelectedAppearanceMode(value: String) {
        selectedAppearanceModeState.value = value
    }

    override suspend fun clearFavoriteNameIds() {
        clearFavoritesCalls++
    }

    override suspend fun resetAll() {
        resetAllCalls++
        dailyReminderEnabledState.value = true
        dailyReminderHourState.value = 9
        dailyReminderMinuteState.value = 0
        selectedLanguageTagState.value = defaultLanguage
        selectedAppearanceModeState.value = defaultAppearance
    }
}
