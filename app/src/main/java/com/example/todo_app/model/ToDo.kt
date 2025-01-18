package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ToDo")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val listId: Int,
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: ToDoStatus = ToDoStatus.NOT_DONE,
    val lastModified: LocalDateTime = LocalDateTime.now(),
    val deadline: LocalDateTime? = null,
)
