package app.asmaquran.mobile.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class AppPreferences(
    private val context: Context
) : SettingsPreferencesStore {
    companion object {
        private val KEY_ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
        private val KEY_QURAN_SYNCED = booleanPreferencesKey("quran_synced")
        private val KEY_FAVORITE_NAME_IDS = stringSetPreferencesKey("favorite_name_ids")
        private val KEY_NOTIFICATIONS_PERMISSION_REQUESTED =
            booleanPreferencesKey("notifications_permission_requested")
        private val KEY_AUTH_PROMPT_COMPLETED = booleanPreferencesKey("auth_prompt_completed")
        private val KEY_DAILY_REMINDER_ENABLED = booleanPreferencesKey("daily_reminder_enabled")
        private val KEY_DAILY_REMINDER_HOUR = intPreferencesKey("daily_reminder_hour")
        private val KEY_DAILY_REMINDER_MINUTE = intPreferencesKey("daily_reminder_minute")
        private val KEY_SELECTED_LANGUAGE_TAG = stringPreferencesKey("selected_language_tag")
        private val KEY_SELECTED_APPEARANCE_MODE =
            stringPreferencesKey("selected_appearance_mode")

        private const val DEFAULT_REMINDER_HOUR = 9
        private const val DEFAULT_REMINDER_MINUTE = 0
        private const val DEFAULT_LANGUAGE_TAG = "ar"
        private const val DEFAULT_APPEARANCE_MODE = "system"
    }

    val onboardingSeen: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_ONBOARDING_SEEN] ?: false
    }

    val quranSynced: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_QURAN_SYNCED] ?: false
    }

    val favoriteNameIds: Flow<Set<Int>> = context.dataStore.data.map { prefs ->
        prefs[KEY_FAVORITE_NAME_IDS]
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()
    }

    val notificationsPermissionRequested: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_NOTIFICATIONS_PERMISSION_REQUESTED] ?: false
    }

    val authPromptCompleted: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_AUTH_PROMPT_COMPLETED] ?: false
    }

    override val dailyReminderEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_DAILY_REMINDER_ENABLED] ?: true
    }

    override val dailyReminderHour: Flow<Int> = context.dataStore.data.map {
        it[KEY_DAILY_REMINDER_HOUR] ?: DEFAULT_REMINDER_HOUR
    }

    override val dailyReminderMinute: Flow<Int> = context.dataStore.data.map {
        it[KEY_DAILY_REMINDER_MINUTE] ?: DEFAULT_REMINDER_MINUTE
    }

    override val selectedLanguageTag: Flow<String> = context.dataStore.data.map {
        it[KEY_SELECTED_LANGUAGE_TAG] ?: DEFAULT_LANGUAGE_TAG
    }

    override val selectedAppearanceMode: Flow<String> = context.dataStore.data.map {
        it[KEY_SELECTED_APPEARANCE_MODE] ?: DEFAULT_APPEARANCE_MODE
    }

    suspend fun setOnboardingSeen(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_SEEN] = value
        }
    }

    suspend fun setQuranSynced(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_QURAN_SYNCED] = value
        }
    }

    suspend fun setFavorite(nameId: Int, isFavorite: Boolean) {
        context.dataStore.edit { prefs ->
            val current = prefs[KEY_FAVORITE_NAME_IDS].orEmpty().toMutableSet()
            val serializedId = nameId.toString()

            if (isFavorite) {
                current += serializedId
            } else {
                current -= serializedId
            }

            prefs[KEY_FAVORITE_NAME_IDS] = current
        }
    }

    suspend fun setNotificationsPermissionRequested(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOTIFICATIONS_PERMISSION_REQUESTED] = value
        }
    }

    override suspend fun setDailyReminderEnabled(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DAILY_REMINDER_ENABLED] = value
        }
    }

    override suspend fun setDailyReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DAILY_REMINDER_HOUR] = hour
            prefs[KEY_DAILY_REMINDER_MINUTE] = minute
        }
    }

    override suspend fun setSelectedLanguageTag(value: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SELECTED_LANGUAGE_TAG] = value
        }
    }

    override suspend fun setSelectedAppearanceMode(value: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SELECTED_APPEARANCE_MODE] = value
        }
    }

    override suspend fun clearFavoriteNameIds() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_FAVORITE_NAME_IDS)
        }
    }

    suspend fun isAuthPromptCompleted(): Boolean {
        return authPromptCompleted.first()
    }

    suspend fun setAuthPromptCompleted(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTH_PROMPT_COMPLETED] = value
        }
    }

    override suspend fun resetAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
