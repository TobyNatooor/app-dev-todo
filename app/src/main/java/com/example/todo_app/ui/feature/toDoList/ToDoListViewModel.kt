package com.example.todo_app.ui.feature.toDoList

import AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoListViewModel(val listId: Int, val db: AppDatabase) : ViewModel() {

    //TODO: Make Query to get list of toDos with listId = listId, and refactor code below

    private val toDos = db.toDoDao().getAll()

    private val mutableToDosState = MutableStateFlow<ToDosUIState>(
        if (toDos.isEmpty()) ToDosUIState.Empty else ToDosUIState.Data(toDos)
    )
    val toDosState: StateFlow<ToDosUIState> = mutableToDosState

    init {
        viewModelScope.launch {


        }
    }

    suspend fun addToDoItem() {
        val newToDo = ToDo(
            title = "New to do item",
            description = "Add Description",
            listId = listId,
            order = 2, //TODO: Add query to find max order
        )
        mutableToDosState.update { currentState ->
            when (currentState) {
                is ToDosUIState.Data -> ToDosUIState.Data(currentState.toDos + newToDo)
                else -> ToDosUIState.Data(listOf(newToDo))
            }
        }
        db.toDoDao().insert(newToDo)
    }

    suspend fun updateToDoItem(updatedToDo: ToDo) {
        db.toDoDao().update(updatedToDo)
        mutableToDosState.update { currentState ->
            when (currentState) {
                is ToDosUIState.Data -> {
                    val updatedList = currentState.toDos.map { todo ->
                        if (todo.id == updatedToDo.id) {
                            updatedToDo
                        } else {
                            todo
                        }
                    }
                    ToDosUIState.Data(updatedList)
                }
                else -> ToDosUIState.Data(listOf(updatedToDo))
            }
        }
    }
}

sealed class ToDosUIState {
    data object Empty : ToDosUIState()
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}