package com.example.todo_app.model

enum class ToDoStatus(private val status: String) {
    NOT_DONE("not done"),
    DONE("done"),
    IN_PROGRESS("in progress"),
    CANCELED("cancelled"),
    CANT_BE_DONE("can't be done");

    // Custom toString for readability
    override fun toString(): String = status

    fun check(): ToDoStatus {
        return when (this) {
            NOT_DONE -> DONE
            else -> NOT_DONE
        }
    }

    fun isDone(): Boolean {
        return this == DONE
    }
}
