package com.example.todo_app.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckList(
    val title : String,
    var toDos : HashMap<Int, ToDo>,
    val description : String,
)
