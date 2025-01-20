package com.example.todo_app.ui.feature.toDoList

import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import com.example.todo_app.model.ToDoStatus
import com.example.todo_app.ui.feature.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val listId: Int,
    db: AppDatabase,
    private val nav: NavController
) : BaseViewModel(db) {

    private val toDos: Flow<List<ToDo>> = db.toDoDao().getAllWithListId(listId)

    val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState

    private val _addingNewToDo = MutableStateFlow(false)
    val addingNewToDo = _addingNewToDo.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()


    init {
        observeFavoriteStatus()
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
        viewModelScope.launch {
            db.toDoDao().insert(newToDo)
            _addingNewToDo.value = false
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

    fun updateList(listId: Int, newTitle: String) {
        viewModelScope.launch {
            db.checkListDao().updateTitle(listId, newTitle)
        }
    }

    fun favoriteClicked(){
        this.viewModelScope.launch {
            db.checkListDao().updateFavorite(listId, !isFavorite.value)
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            db.checkListDao().isFavorite(listId)
                .collect { favorite ->
                    _isFavorite.value = favorite
                }
        }
    }

    fun favoriteClicked(){
        this.viewModelScope.launch {
            db.checkListDao().updateFavorite(listId, !isFavorite.value)
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            db.checkListDao().isFavorite(listId)
                .collect { favorite ->
                    _isFavorite.value = favorite
                }
        }
    }

    fun favoriteClicked(){
        this.viewModelScope.launch {
            db.checkListDao().updateFavorite(listId, !isFavorite.value)
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            db.checkListDao().isFavorite(listId)
                .collect { favorite ->
                    _isFavorite.value = favorite
                }
        }
    }

    fun favoriteClicked(){
        this.viewModelScope.launch {
            db.checkListDao().updateFavorite(listId, !isFavorite.value)
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            db.checkListDao().isFavorite(listId)
                .collect { favorite ->
                    _isFavorite.value = favorite
                }
        }
    }

    fun deleteList(listId: Int) {
        viewModelScope.launch {
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
