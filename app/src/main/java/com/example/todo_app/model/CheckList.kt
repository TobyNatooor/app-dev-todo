package com.example.todo_app.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckList(
    val title : String,
    val toDos : Array<ToDo>,
    val description : String,
)
