package com.example.todo_app.ui.feature

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.ToDo
import com.example.todo_app.model.ToDoStatus
import com.example.todo_app.repository.ToDoRepoImpl
import com.example.todo_app.repository.ToDoRepository
import com.example.todo_app.ui.feature.home.NewListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

abstract class BaseViewModel: ViewModel() {
    protected val toDoRepo: ToDoRepository = ToDoRepoImpl.getInstance()

    private val _shouldShowCongratsGif = MutableStateFlow(false)
    val shouldShowCongratsGifState: StateFlow<Boolean> = _shouldShowCongratsGif.asStateFlow()

    protected abstract val toDos: Flow<List<ToDo>>

    fun updateToDoItem(updatedToDo: ToDo) {
        viewModelScope.launch {
            toDoRepo.update(updatedToDo.copy(lastModified = LocalDateTime.now()))
            toDos.collect { todos ->
                _shouldShowCongratsGif.value = getIfShouldShowCongratsGif(todos)
                Log.d("ABCDEF", "${_shouldShowCongratsGif.value}")
            }
        }
    }

    fun deleteToDo(toDo: ToDo) {
        viewModelScope.launch {
            toDoRepo.delete(toDo)
        }
    }

    private fun getIfShouldShowCongratsGif(todos: List<ToDo>): Boolean {
        todos.forEach { todo ->
            if (todo.status != ToDoStatus.DONE && todo.status != ToDoStatus.CANCELED) {
                return false
            }
        }
        return true
    }

    fun dismissCongratsGif() {
        _shouldShowCongratsGif.value = false
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
