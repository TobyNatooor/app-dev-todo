package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.data.AppDatabase

class SmartListViewModelFactory(
    private val db: AppDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmartListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmartListViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
