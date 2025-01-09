package com.example.todo_app.ui.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.SortOption
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.theme.list
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(val db: AppDatabase, val nav: NavController) : ViewModel() {

    private val lists: Flow<List<CheckList>> = db.checkListDao().getAll()
    private val todos: Flow<List<ToDo>> = flowOf(ArrayList())
    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState

    init {
        viewModelScope.launch {
            combine(lists, todos) { list, todo ->
                if (list.isEmpty()) {
                    HomeUIState.Empty
                } else {
                    HomeUIState.Data(list, todo)
                }
            }.collect { homeUIState ->
                _mutableHomeState.value = homeUIState
            }
        }
    }

    fun getTodosByListId(listId: Int): List<ToDo> {
        val todos: List<ToDo> = when (val hs = homeState.value) {
            is HomeUIState.Data -> hs.todos.filter { todo -> todo.listId == listId }
            HomeUIState.Empty -> ArrayList()
            HomeUIState.Loading -> ArrayList()
        }
        return todos
    }

    fun searchTodos(string: String) {
        this.viewModelScope.launch {
            val todos: Flow<List<ToDo>> = if (string.isEmpty()) {
                flowOf(ArrayList())
            } else {
                db.toDoDao().findWithTitle(string)
            }
            combine(lists, todos) { list, todo ->
                if (list.isEmpty()) {
                    HomeUIState.Empty
                } else {
                    HomeUIState.Data(list, todo)
                }
            }.collect { homeUIState ->
                _mutableHomeState.value = homeUIState
            }
        }
    }

    fun addList() {
        this.viewModelScope.launch {
            val newList = CheckList(
                title = null,
                description = "Add Description",
                //created = LocalDateTime.now(),
                order = 2, //TODO: Add query to find max order
                folderId = 0
            )
            db.checkListDao().insert(newList)
        }
    }

    fun sortLists(sortBy: SortOption){
        this.viewModelScope.launch {
            var sortedList : Flow<List<CheckList>> = lists
            when (sortBy) {
                SortOption.CREATED -> sortedList = db.checkListDao().getAllSortedByCreated()
                SortOption.RECENT -> {}
                SortOption.NAME -> sortedList = db.checkListDao().getAllSortedByName()
            }
            combine(sortedList, todos) { list, todo -> _mutableHomeState.value = HomeUIState.Data(list, todo)}
        }
    }

    fun updateList(list: CheckList){
        this.viewModelScope.launch {
            db.checkListDao().update(list)
        }
    }

    fun clickList(listTitle: String, listId: Int){
        nav.navigate("todoList/${listTitle}/${listId}")
    }
}

sealed class HomeUIState {
    data object Empty : HomeUIState()
    data object Loading : HomeUIState()
    data class Data(val lists: List<CheckList>, val todos: List<ToDo>) : HomeUIState()
}
