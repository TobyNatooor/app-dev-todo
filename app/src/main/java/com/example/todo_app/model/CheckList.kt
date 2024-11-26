package com.example.todo_app.model

import kotlinx.serialization.Serializable

@Serializable
data class CheckList(
    val id : Int,
    val title : String,
    val description : String,
)
