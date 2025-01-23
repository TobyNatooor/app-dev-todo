package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.data.AppDatabase
import androidx.navigation.NavController
import com.example.todo_app.repository.ChecklistRepository
import com.example.todo_app.repository.ToDoRepository
import com.example.todo_app.repository.UserRepository

class SmartListViewModelFactory(
    private val toDoRepository: ToDoRepository,
    private val checklistRepository: ChecklistRepository,
    private val userRepository: UserRepository,
    private val nav: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmartListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmartListViewModel(toDoRepository, checklistRepository, userRepository, nav) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
