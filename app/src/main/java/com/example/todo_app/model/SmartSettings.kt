package com.example.todo_app.model

data class SmartSettings(
    val includeNotDone: Boolean = false,
    val includeInProgress: Boolean = false,
    val includeDone: Boolean = false,
    val includeCancelled: Boolean = false,
    val listId: Int = -1,
    val deadlineWithinDays: Int = 0,
)
