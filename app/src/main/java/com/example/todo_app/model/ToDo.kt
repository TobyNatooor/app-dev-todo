package com.example.todo_app.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "ToDo")
data class ToDo @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String? = null,
    val description: String,
    val created : LocalDateTime = LocalDateTime.now(),
    val listId: Int,
    val order: Int,
    val status: ToDoStatus = ToDoStatus.NOT_DONE,
    //val doneWhen: LocalDateTime? = null - TODO: Room cannot save this variable.
)
