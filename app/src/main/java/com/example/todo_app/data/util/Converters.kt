package com.example.todo_app.data.util

import androidx.room.TypeConverter
import com.example.todo_app.model.ToDoStatus
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromToDoStatus(status: ToDoStatus): String = status.name

    @TypeConverter
    fun toToDoStatus(value: String): ToDoStatus = ToDoStatus.valueOf(value)
}
