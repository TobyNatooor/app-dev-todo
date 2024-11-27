package com.example.todo_app.ui.feature.home

import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigator

class HomeViewModelFactory(
    val db: AppDatabase,
    val nav: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(db, nav) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}