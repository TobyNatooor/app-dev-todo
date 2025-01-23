package com.example.todo_app.ui.feature.toDoOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToDoOptionsViewModelFactory (
    private val toDoId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoOptionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoOptionsViewModel(toDoId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
