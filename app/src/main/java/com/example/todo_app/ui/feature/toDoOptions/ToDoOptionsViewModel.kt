package com.example.todo_app.ui.feature.toDoOptions

import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ToDoOptionsViewModel(
    private val toDoId: Int,
    db: AppDatabase
) : BaseViewModel(db) {

    private val _toDoState = MutableStateFlow<ToDoUIState>(ToDoUIState.Loading)
    val toDoState: StateFlow<ToDoUIState> = _toDoState

    init {
        loadToDo()
    }

    private fun loadToDo() {
        viewModelScope.launch {
            val toDoFlow: Flow<ToDo> = db.toDoDao().getWithId(toDoId)
            toDoFlow.collect { toDo ->
                val toDos: Flow<List<CheckList>> = db.checkListDao().getAllSortedByName()
                toDos.collect { checklists ->
                    _toDoState.value = ToDoUIState.Data(toDo, checklists)
                }
            }
        }
    }

    fun updateToDo(updatedToDo: ToDo) {
        viewModelScope.launch {
            val toDoToUpdate = if (updatedToDo.title.isNullOrBlank()) {
                updatedToDo.copy(title = "Unnamed to do item")
            } else {
                updatedToDo
            }
            db.toDoDao().update(toDoToUpdate.copy(lastModified = LocalDateTime.now()))
            loadToDo()
        }
    }
}

sealed class ToDoUIState {
    data object Loading : ToDoUIState()
    data class Data(val toDo: ToDo, val checklists: List<CheckList>) : ToDoUIState()
}
