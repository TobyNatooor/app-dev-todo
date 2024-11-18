package com.example.todo_app.ui.feature.toDoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.data.DataHandler

class ToDoListViewModelFactory(
    private val listId: Int,
    private val dataHandler: DataHandler
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ToDoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ToDoListViewModel(listId, dataHandler) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}