package com.example.todo_app.ui.feature.toDoList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.model.ToDo
import com.example.todo_app.repository.ToDoRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoListViewModel() : ViewModel() {

    private val toDoRepo = ToDoRepo()

    private val mutableToDosState = MutableStateFlow<ToDosUIState>(
        if (toDoRepo.toDos.isEmpty()) ToDosUIState.Empty else ToDosUIState.Data(toDoRepo.toDos)
    )
    val toDosState: StateFlow<ToDosUIState> = mutableToDosState

    init {
        viewModelScope.launch {


        }
    }

    fun addToDoItem(newToDo: ToDo) {
        mutableToDosState.update { currentState ->
            when (currentState) {
                is ToDosUIState.Data -> ToDosUIState.Data(currentState.toDos + newToDo)
                else -> ToDosUIState.Data(listOf(newToDo))
            }
        }
    }

}

sealed class ToDosUIState {
    data object Empty: ToDosUIState()
    data object Loading: ToDosUIState()
    data class Data(val toDos: List<ToDo>): ToDosUIState()
}