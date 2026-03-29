package app.asmaquran.mobile.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class AppPreferences(
    private val context: Context
) {
    companion object {
        private val KEY_ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
        private val KEY_QURAN_SYNCED = booleanPreferencesKey("quran_synced")
        private val KEY_FAVORITE_NAME_IDS = stringSetPreferencesKey("favorite_name_ids")
        private val KEY_NOTIFICATIONS_PERMISSION_REQUESTED =
            booleanPreferencesKey("notifications_permission_requested")
        private val KEY_AUTH_PROMPT_COMPLETED = booleanPreferencesKey("auth_prompt_completed")
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

    suspend fun isAuthPromptCompleted(): Boolean {
        return authPromptCompleted.first()
    }

    suspend fun setAuthPromptCompleted(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_AUTH_PROMPT_COMPLETED] = value
        }
    }
}
