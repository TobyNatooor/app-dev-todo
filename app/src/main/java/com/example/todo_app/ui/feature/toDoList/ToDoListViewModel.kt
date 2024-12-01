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


    private val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState

    init {
        viewModelScope.launch {
            val toDos: Flow<List<ToDo>> = db.toDoDao().getAllWithListId(listId)
            toDos.collect { list ->
                _mutableToDosState.value = ToDosUIState.Data(list)
            }

        }
    }

    fun addToDoItem() {
        val newToDo = ToDo(
            title = null,
            description = "Add Description",
            listId = listId,
            order = -1, //TODO: Add query to find max order
        )
        this.viewModelScope.launch {
            db.toDoDao().insert(newToDo)
        }
    }

    fun updateToDoItem(updatedToDo: ToDo) {
        println("Updating item")
        this.viewModelScope.launch {
            db.toDoDao().update(updatedToDo)
        }
    }

    fun deleteToDo(toDo: ToDo){
        this.viewModelScope.launch {
            db.toDoDao().delete(toDo)
        }
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
