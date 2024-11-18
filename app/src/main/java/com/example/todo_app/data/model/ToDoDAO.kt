package com.example.todo_app.data.model

import com.example.todo_app.model.ToDo

data class ToDoDAO (
    var id : Int,
    var title : String,
    var isDone : Boolean,
    var description : String,
    var listId : Int,
){
    fun overrideWith(toDo : ToDo){
        this.id = toDo.id
        this.title = toDo.title
        this.isDone = toDo.isDone
        this.description = toDo.description
    }
}