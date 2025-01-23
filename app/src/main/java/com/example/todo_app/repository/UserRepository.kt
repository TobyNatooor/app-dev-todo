package com.example.todo_app.repository

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.todo_app.model.SmartSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserRepository(private val dataStore: DataStore<Preferences>) {
    private companion object {
        val NOT_DONE = booleanPreferencesKey("not_done")
        val IN_PROGRESS = booleanPreferencesKey("in_progress")
        val DONE = booleanPreferencesKey("done")
        val CANCELLED = booleanPreferencesKey("cancelled")
        val LESS_THAN_DAYS = intPreferencesKey("less_than_days")
        val LIST_ID = intPreferencesKey("list_id")
    }

    private val _smartSettings = MutableStateFlow(SmartSettings())
    val smartSettings: StateFlow<SmartSettings> = _smartSettings

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.collect { preferences ->
                _smartSettings.value = SmartSettings(
                    includeNotDone = preferences[NOT_DONE] ?: false,
                    includeInProgress = preferences[IN_PROGRESS] ?: false,
                    includeDone = preferences[DONE] ?: false,
                    includeCancelled = preferences[CANCELLED] ?: false,
                    deadlineWithinDays = preferences[LESS_THAN_DAYS] ?: 0,
                    listId = preferences[LIST_ID] ?: -1
                )
            }
        }
    }

    suspend fun updateSettings(settings: SmartSettings) {
        dataStore.edit { preferences ->
            preferences[NOT_DONE] = settings.includeNotDone
            preferences[IN_PROGRESS] = settings.includeInProgress
            preferences[DONE] = settings.includeDone
            preferences[CANCELLED] = settings.includeCancelled
            preferences[LESS_THAN_DAYS] = settings.deadlineWithinDays
            preferences[LIST_ID] = settings.listId
        }
    }
}