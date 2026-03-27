package com.example.allahnamesquran.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class AppPreferences(
    private val context: Context
) {
    companion object {
        private val KEY_ONBOARDING_SEEN = booleanPreferencesKey("onboarding_seen")
        private val KEY_QURAN_SYNCED = booleanPreferencesKey("quran_synced")
    }

    val onboardingSeen: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_ONBOARDING_SEEN] ?: false
    }

    val quranSynced: Flow<Boolean> = context.dataStore.data.map {
        it[KEY_QURAN_SYNCED] ?: false
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
}