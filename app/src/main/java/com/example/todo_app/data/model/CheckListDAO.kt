package com.example.todo_app.data.model

import com.example.todo_app.model.CheckList

data class CheckListDAO (
    var id : Int,
    var title : String,
    var description : String,
){
    fun overrideWith(list : CheckList){
        this.id = list.id
        this.title = list.title
        this.description = list.description
    }
}