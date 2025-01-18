package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.data.AppDatabase
import androidx.navigation.NavController

class SmartListViewModelFactory(
    private val db: AppDatabase,
    private val nav: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmartListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmartListViewModel(db, nav) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
