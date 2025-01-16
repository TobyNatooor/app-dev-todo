package com.example.todo_app.model

enum class SortOption(private val status: String) {
    NAME("Name"),
    CREATED("Date"),
    RECENT("Recent");

    override fun toString(): String {
        return this.status
    }
}