package com.example.todo_app.ui.feature.toDoList

import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ToDoListViewModel(val listId: Int, val db: AppDatabase) : ViewModel() {

    private val toDos: Flow<List<ToDo>> = db.toDoDao().getAllWithListId(listId)
    private val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState

    init {
        viewModelScope.launch {
            toDos.collect { list ->
                _mutableToDosState.value = if (list.isEmpty()) {
                    ToDosUIState.Empty
                } else {
                    ToDosUIState.Data(list)
                }
            }

        }
    }

    suspend fun addToDoItem() {
        val newToDo = ToDo(
            title = "New to do item",
            description = "Add Description",
            listId = listId,
            order = 2, //TODO: Add query to find max order
        )
        db.toDoDao().insert(newToDo)
    }

    suspend fun updateToDoItem(updatedToDo: ToDo) {
        db.toDoDao().update(updatedToDo)
    }

}

sealed class ToDosUIState {
    data object Empty : ToDosUIState()
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
