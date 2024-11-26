package com.example.todo_app.ui.feature.toDoList

import AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ToDoListViewModelFactory(
    val listId: Int,
    val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoListViewModel(listId, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}