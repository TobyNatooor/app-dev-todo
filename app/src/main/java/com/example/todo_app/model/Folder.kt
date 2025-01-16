package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Folder")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val parentId: Int,
)