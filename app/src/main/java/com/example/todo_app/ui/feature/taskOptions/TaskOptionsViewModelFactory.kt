package com.example.todo_app.ui.feature.taskOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.data.AppDatabase

class TaskOptionsViewModelFactory (
    private val taskId: Int,
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskOptionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskOptionsViewModel(taskId, db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
