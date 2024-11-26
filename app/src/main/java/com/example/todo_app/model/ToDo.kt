package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ToDo")
data class ToDo (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val listId: Int,
    val order: Int,
    val status: String = "unDone",
    val doneWhen: LocalDateTime
    )