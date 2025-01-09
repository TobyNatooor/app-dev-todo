package com.example.todo_app.ui.feature.toDoOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ToDoOptionsViewModel(private val toDoId: Int, private val db: AppDatabase) : ViewModel() {
    private val _mutableToDoState = MutableStateFlow<ToDoUIState>(ToDoUIState.Loading)
    val toDoState: StateFlow<ToDoUIState> = _mutableToDoState

    init {
        viewModelScope.launch {
            val toDoFlow: Flow<ToDo> = db.toDoDao().getWithId(toDoId)
            toDoFlow.collect { toDo ->
                val toDos: Flow<List<ToDo>> = db.toDoDao().getAllWithListId(toDo.listId)
                toDos.collect { list ->
                    val sortedList = list.sortedWith(compareBy { it.order })
                    _mutableToDoState.value = ToDoUIState.Data(toDo, sortedList)
                }
            }
        }
    }

    fun updateToDo(updatedToDo: ToDo) {
        val toDoToUpdate = if (updatedToDo.title.isNullOrBlank()) {
            updatedToDo.copy(title = "Unnamed to do item")
        } else {
            updatedToDo
        }

        viewModelScope.launch {
            db.toDoDao().update(toDoToUpdate)
        }
    }
}

sealed class ToDoUIState {
    data object Loading : ToDoUIState()
    data class Data(val toDo: ToDo, val toDoList: List<ToDo>) : ToDoUIState()
}
