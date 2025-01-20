package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

//@Entity(tableName = "settings")
data class SmartSettings(
    //@PrimaryKey(autoGenerate = true)
    val includeNotDone: Boolean = false,
    val includeInProgress: Boolean = false,
    val includeDone: Boolean = false,
    val includeCancelled: Boolean = false,
    val listId: Int? = null,
    val includeDeadlineLessThan: Boolean = false,
    val DeadlineLessThanDays: Int = 0,
    //val includeAddedBeforeDate: Boolean = false,
    //val includeAddedAfterDate: Boolean = false,
    //val includeDurationLessThan: Boolean = false,
    //val AddedBeforeDate: LocalDateTime? = null,
    //val AddedAfterDate: LocalDateTime? = null,
    //val DurationLessThanMin: Int = 0,
)

object SmartSettingsSingleton {
    // Internal mutable state
    private val _settings = MutableStateFlow(SmartSettings())

    // Exposed state as an immutable StateFlow
    val settings: StateFlow<SmartSettings> = _settings.asStateFlow()

    // Function to update the settings
    fun updateSettings(newSettings: SmartSettings) {
        _settings.value = newSettings
    }

    // Function to reset settings to defaults
    fun resetSettings() {
        _settings.value = SmartSettings()
    }
}