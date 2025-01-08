package com.example.todo_app.data.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.example.todo_app.model.ToDoStatus
import java.time.LocalDateTime

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }
}
