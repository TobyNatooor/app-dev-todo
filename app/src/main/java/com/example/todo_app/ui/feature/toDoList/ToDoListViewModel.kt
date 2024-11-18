package com.example.todo_app.ui.feature.toDoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.DataHandler
import com.example.todo_app.model.ToDo
import com.example.todo_app.repository.ToDoRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoListViewModel(private val listId: Int, private val dataHandler: DataHandler) : ViewModel() {

    private val toDos = dataHandler.getToDos(listId)

    private val mutableToDosState = MutableStateFlow<ToDosUIState>(
        if (toDos.isEmpty()) ToDosUIState.Empty else ToDosUIState.Data(toDos)
    )
    val toDosState: StateFlow<ToDosUIState> = mutableToDosState

    init {
        viewModelScope.launch {


        }
    }

    fun addToDoItem() {
        var newToDo = ToDo(
            id = dataHandler.newToDoId(),
            title = "New to do item",
            isDone = false,
            description = "Add Description"
        )
        mutableToDosState.update { currentState ->
            when (currentState) {
                is ToDosUIState.Data -> ToDosUIState.Data(currentState.toDos + newToDo)
                else -> ToDosUIState.Data(listOf(newToDo))
            }
        }
        dataHandler.save(newToDo, listId)
    }

    fun updateToDoItem(updatedToDo: ToDo) {
        mutableToDosState.update { currentState ->
            when (currentState) {
                is ToDosUIState.Data -> {
                    val updatedList = currentState.toDos.map { todo ->
                        if (todo.title == updatedToDo.title) {
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