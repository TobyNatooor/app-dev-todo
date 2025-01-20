package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

//@Entity(tableName = "settings")
data class SmartSettings(
    //@PrimaryKey(autoGenerate = true)
    val includeNotDone: Boolean = false,
    val includeInProgress: Boolean = false,
    val includeDone: Boolean = false,
    val includeCancelled: Boolean = false,
    val includeFromList: Boolean = false,
    val listId: Int = -1,
    val includeDeadlineLessThan: Boolean = false,
    val DeadlineLessThanDays: Int = 0,
    //val includeAddedBeforeDate: Boolean = false,
    //val includeAddedAfterDate: Boolean = false,
    //val includeDurationLessThan: Boolean = false,
    //val AddedBeforeDate: LocalDateTime? = null,
    //val AddedAfterDate: LocalDateTime? = null,
    //val DurationLessThanMin: Int = 0,
)