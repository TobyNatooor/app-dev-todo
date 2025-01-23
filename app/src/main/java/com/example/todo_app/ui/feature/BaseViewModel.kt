package com.example.todo_app.ui.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.ToDo
import com.example.todo_app.repository.ToDoRepoImpl
import com.example.todo_app.repository.ToDoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

abstract class BaseViewModel: ViewModel() {
    protected val toDoRepo = ToDoRepoImpl.getInstance()

    fun updateToDoItem(updatedToDo: ToDo) {
        viewModelScope.launch {
            toDoRepo.update(updatedToDo.copy(lastModified = LocalDateTime.now()))
        }
    }

    fun deleteToDo(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepo.delete(toDo)
        }
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
