package com.example.todo_app.ui.feature.toDoList

import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import com.example.todo_app.model.ToDoStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val listId: Int,
    private val db: AppDatabase,
    private val nav: NavController
) : ViewModel() {

    private val toDos: Flow<List<ToDo>> = db.toDoDao().getAllWithListId(listId)

    private val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState

    private val _addingNewToDo = MutableStateFlow(false)
    val addingNewToDo = _addingNewToDo.asStateFlow()



    init {
        viewModelScope.launch {
            toDos.collect { list ->
                _mutableToDosState.value = ToDosUIState.Data(list.sortedBy { it.status })
            }
        }
    }

    fun addToDoItem(title: String) {
        val newToDo = ToDo(
            title = title,
            description = "Add Description",
            listId = listId,
        )
        this.viewModelScope.launch {
            db.toDoDao().insert(newToDo)
            _addingNewToDo.value = false
        }
    }

    fun updateToDoItem(updatedToDo: ToDo) {
        this.viewModelScope.launch {
            db.toDoDao().update(updatedToDo)
        }
    }

    fun clickToDoOptions(toDoId: Int) {
        nav.navigate("toDoOptions/${toDoId}")
    }

    fun deleteToDo(toDo: ToDo) {
        this.viewModelScope.launch {
            db.toDoDao().delete(toDo)
        }
    }

    fun updateList(listId: Int, newTitle: String) {
        this.viewModelScope.launch {
            db.checkListDao().updateTitle(listId, newTitle)
        }
    }

    fun deleteList(listId: Int) {
        this.viewModelScope.launch {
            db.checkListDao().deleteWithId(listId)
            nav.navigate("home")
        }
    }

    fun addClicked(){
        _addingNewToDo.value = true
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
