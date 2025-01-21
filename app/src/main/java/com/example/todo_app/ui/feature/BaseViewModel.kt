package com.example.todo_app.ui.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

abstract class BaseViewModel(
    protected val db: AppDatabase
) : ViewModel() {

    //protected val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    //val toDosState: StateFlow<ToDosUIState> = _mutableToDosState.asStateFlow()

    fun updateToDoItem(updatedToDo: ToDo) {
        viewModelScope.launch {
            db.toDoDao().update(updatedToDo.copy(lastModified = LocalDateTime.now()))
        }
    }

    fun deleteToDo(toDo: ToDo) {
        viewModelScope.launch {
            db.toDoDao().delete(toDo)
        }
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
