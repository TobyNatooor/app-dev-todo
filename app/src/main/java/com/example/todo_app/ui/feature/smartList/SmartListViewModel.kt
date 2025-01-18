package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.ui.feature.BaseViewModel
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SmartListViewModel(
    db: AppDatabase,
    private val nav: NavController
) : BaseViewModel(db) {

    val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState.asStateFlow()

    init {
        viewModelScope.launch {
            db.toDoDao().getAll().collect { list ->
                _mutableToDosState.value = ToDosUIState.Data(list)
            }
        }
    }

    /*override fun updateToDoItem(updatedToDo: ToDo) {
        viewModelScope.launch {
            db.toDoDao().update(updatedToDo)
        }
    }*/

    fun clickToDoOptions(toDoId: Int) {
        nav.navigate("toDoOptions/${toDoId}")
    }

    /*override fun deleteToDo(toDo: ToDo) {
        viewModelScope.launch {
            db.toDoDao().delete(toDo)
        }
    }*/
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
