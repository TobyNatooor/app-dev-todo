package com.example.todo_app.ui.feature.home

import android.health.connect.changelog.ChangeLogsResponse.DeletedLog
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class HomeViewModel(private val db: AppDatabase, private val nav: NavController) : ViewModel() {
    var sortedOption: SortOption = SortOption.NAME
    private var lists: Flow<List<CheckList>> = listBySort(sortedOption)
    private val todos: Flow<List<ToDo>> = flowOf(ArrayList())
    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState
    var currentChar: Char = '\u0000'

    init {
        viewModelScope.launch {
            combine(lists, todos) { list, todo ->
                lists = listBySort(sortedOption)
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

            val lists: Flow<List<CheckList>> = if (string.isEmpty()) {
                db.checkListDao().getAll()
            } else {
                db.checkListDao().findWithTodosTitle(string)
            }

            combine(lists, todos) { list, todo ->
                if (list.isEmpty()) {
                    //HomeUIState.Empty
                    HomeUIState.Data(ArrayList(),ArrayList())
                } else {
                    HomeUIState.Data(list, todo)
                }
            }.collect { homeUIState ->
                Log.d("TODOS", "ui state: $homeUIState")
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

    fun sortLists(sortBy: SortOption) {
        sortedOption = sortBy
        this.viewModelScope.launch {
            lists = listBySort(sortBy)
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

    fun updateList(list: CheckList) {
        this.viewModelScope.launch {
            db.checkListDao().update(list)
        }
    }

    fun clickList(list: CheckList) {
        this.viewModelScope.launch {
            db.checkListDao().update(list.copy(lastModified = LocalDateTime.now()))
        }
        nav.navigate("todoList/${list.title}/${list.id}")
    }

    private fun listBySort(sortBy: SortOption): Flow<List<CheckList>> {
        return when (sortBy) {
            SortOption.CREATED -> db.checkListDao().getAllSortedByCreated()
            SortOption.RECENT -> db.checkListDao().getAllSortedByLastModified()
            SortOption.NAME -> db.checkListDao().getAllSortedByName()
        }

    }

    fun isNextChar(list: CheckList): Char{
        if(!list.title.isNullOrEmpty()){
            val char = list.title[0].uppercaseChar()
            println("Name of list '${list.title} -- First char '$char' -- Current char '$currentChar'")
            if(char != currentChar){
                currentChar = char
                return currentChar
            }
        }
        return '!'
    }
}

sealed class HomeUIState {
    data object Empty : HomeUIState()
    data object Loading : HomeUIState()
    data class Data(val lists: List<CheckList>, val todos: List<ToDo>) : HomeUIState()
}
