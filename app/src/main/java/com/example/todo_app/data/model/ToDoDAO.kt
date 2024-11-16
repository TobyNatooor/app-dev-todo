package com.example.todo_app.data.model

data class ToDoDAO (
    val id : Int,
    val title : String,
    val isDone : Boolean,
    val description : String,
    val listId : Int,
)