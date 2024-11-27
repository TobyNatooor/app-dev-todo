package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ToDo")
data class ToDo (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val listId: Int,
    val order: Int,
    val status: ToDoStatus = ToDoStatus.UN_DONE,
    //val doneWhen: LocalDateTime? = null - TODO: Room cannot save this variable.
)
