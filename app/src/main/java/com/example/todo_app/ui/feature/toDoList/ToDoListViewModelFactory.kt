package com.example.todo_app.ui.feature.toDoList

import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.example.todo_app.repository.ChecklistRepository
import com.example.todo_app.repository.ToDoRepository

class ToDoListViewModelFactory(
    private val listId: Int,
    private val nav: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoListViewModel(listId, nav) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
