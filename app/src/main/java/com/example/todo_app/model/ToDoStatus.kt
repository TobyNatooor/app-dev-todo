package com.example.todo_app.model

enum class ToDoStatus(val status: String) {
    UN_DONE("unDone"),
    DOING("doing"),
    DONE("done");

    override fun toString(): String = status // Custom toString for readability

    fun check(): ToDoStatus {
        return when (this) {
            UN_DONE -> DOING
            DOING -> DONE
            DONE -> UN_DONE
        }
    }

    fun isDone(): Boolean {
        if (this == DONE) return true
        else return false
    }
}
