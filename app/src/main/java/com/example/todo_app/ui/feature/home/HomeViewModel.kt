package com.example.todo_app.ui.feature.home

import android.util.Log
import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.SortOption
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(private val db: AppDatabase, private val nav: NavController) : ViewModel() {
    private val _sortingOption = MutableStateFlow(SortOption.NAME)
    val sortedOption: StateFlow<SortOption> = _sortingOption.asStateFlow()
    private var lists: Flow<List<CheckList>> = combine(
        db.checkListDao().getAll(),
        _sortingOption
        )
        { list, sort ->
        delay(100)
        when (sort) {
            SortOption.CREATED -> list.sortedByDescending { it.created }
            SortOption.NAME -> list.sortedBy { it.title?.lowercase() }
            SortOption.RECENT -> list.sortedByDescending { it.lastModified }
        }
    }
    private val todos: Flow<List<ToDo>> = flowOf(ArrayList())
    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState
    var currentChar: Char = '\u0000'

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

    fun searchForTodos(query: String) {
        this.viewModelScope.launch {
            val todos: Flow<List<ToDo>> = if (query.isEmpty()) {
                flowOf(ArrayList())
            } else {
                db.toDoDao().findWithTitle(query)
            }

            val lists: Flow<List<CheckList>> = if (query.isEmpty()) {
                db.checkListDao().getAll()
            } else {
                db.checkListDao().findWithTodosTitle(query)
            }

            combine(lists, todos) { list, todo ->
                if (list.isEmpty()) {
                    //HomeUIState.Empty
                    HomeUIState.Data(ArrayList(), ArrayList())
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
                order = 2, //TODO: Add query to find max order
                folderId = 0
            )
            db.checkListDao().insert(newList)
        }
    }

    fun sortLists(sortBy: SortOption) {
        _sortingOption.value = sortBy
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

    fun isNextChar(list: CheckList): Char {
        if (!list.title.isNullOrEmpty()) {
            val char = list.title[0].uppercaseChar()
            println("Name of list '${list.title} -- First char '$char' -- Current char '$currentChar'")
            if (char != currentChar) {
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
