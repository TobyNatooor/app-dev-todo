package com.example.todo_app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CheckList")
data class CheckList(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String? = null,
    val description : String,
    val order: Int,
    val folderId: Int,
)
