package com.example.todo_app.model

import kotlinx.serialization.Serializable

@Serializable
data class List(
    var name: String,
    var toDos: Array<ToDo>,
    var description: String,
)
