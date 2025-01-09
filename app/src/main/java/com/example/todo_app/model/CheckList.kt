package com.example.todo_app.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "CheckList")
data class CheckList @RequiresApi(Build.VERSION_CODES.O) constructor(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String? = null,
    val description : String,
    val created : LocalDateTime = LocalDateTime.now(),
    val lastModified : LocalDateTime = LocalDateTime.now(),
    val order : Int,
    val folderId : Int,
)
