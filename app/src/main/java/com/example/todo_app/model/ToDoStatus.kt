package com.example.todo_app.model

import androidx.room.TypeConverter

enum class ToDoStatus(val status: String) {
    NOT_DONE("not done"),
    DONE("done"),
    IN_PROGRESS("in progress"),
    CANCELED("cancelled"),
    CANT_BE_DONE("can't be done");


    override fun toString(): String = status // Custom toString for readability

    fun check(): ToDoStatus {
        return when(this) {
            NOT_DONE -> DONE
            else -> NOT_DONE
        }
    }

    fun isDone(): Boolean {
        if (this == DONE) return true
        else return false
    }
}

class ToDoStatusConverter {
    @TypeConverter
    fun fromToDoStatus(status: ToDoStatus): String = status.name

    @TypeConverter
    fun toToDoStatus(value: String): ToDoStatus = ToDoStatus.valueOf(value)
}
