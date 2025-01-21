package com.example.todo_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "ToDo",
    foreignKeys = [
        ForeignKey(
            entity = CheckList::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("listId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val created: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "listId")
    val listId: Int,
    val location: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val status: ToDoStatus = ToDoStatus.NOT_DONE,
    val lastModified: LocalDateTime = LocalDateTime.now(),
    val deadline: LocalDateTime? = null,
)
