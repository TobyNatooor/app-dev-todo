package com.example.todo_app.model

import java.util.Date
import kotlinx.serialization.Serializable

@Serializable
data class ToDo (
    var title: String,
    var isDone: Boolean? = false,
    var deadline: Date,
    var description: String,
    )