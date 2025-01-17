package com.example.todo_app.ui.feature.home

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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(private val db: AppDatabase, private val nav: NavController) : ViewModel() {
    private val _sortingOption = MutableStateFlow(SortOption.NAME)
    val sortedOption: StateFlow<SortOption> = _sortingOption.asStateFlow()

    private val favoriteList: Flow<List<CheckList>> = db.checkListDao().getAllFavorites()

    private val _filterQuery = MutableStateFlow("")
    val filteringQuery = _filterQuery.asStateFlow()

    private val filteredList: Flow<List<CheckList>> = filteringQuery.flatMapLatest { query ->
        if (query.isBlank()) db.checkListDao().getAllNonFavorite()
        else db.checkListDao().findWithTodosTitle(query)
    }

    private val lists: Flow<List<CheckList>> = combine(
        filteredList,
        _sortingOption
    ) { list, sort ->
        delay(100)
        when (sort) {
            SortOption.CREATED -> list.sortedByDescending { it.created }
            SortOption.NAME -> list.sortedBy { it.title.lowercase() }
            SortOption.RECENT -> list.sortedByDescending { it.lastModified }
        }
    }
    private val todos: Flow<List<ToDo>> =  filteringQuery.flatMapLatest { query ->
        if (query.isBlank()) flowOf(ArrayList())
        else db.toDoDao().findWithTitle(query)
    }

    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState

    private val _addingNewList = MutableStateFlow(false)
    val addingNewList = _addingNewList.asStateFlow()

    init {
        viewModelScope.launch {
            combine(favoriteList, lists, todos) { favorites, list, todo ->
                if (list.isEmpty()) {
                    HomeUIState.Empty
                } else {
                    HomeUIState.Data(favorites, list, todo)
                }
            }.collect { homeUIState ->
                _mutableHomeState.value = homeUIState
            }
        }
    }

    fun getQuery(): String{
        return filteringQuery.value
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
        _filterQuery.value = query
    }

    fun addList(title: String) {
        this.viewModelScope.launch {
            val newList = CheckList(
                title = title,
                description = "Add Description",
            )
            db.checkListDao().insert(newList)
            _addingNewList.value = false
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

    fun deleteList(listId: Int) {
        this.viewModelScope.launch {
            db.checkListDao().deleteWithId(listId)
        }
    }

    fun clickList(list: CheckList) {
        this.viewModelScope.launch {
            db.checkListDao().update(list.copy(lastModified = LocalDateTime.now()))
        }
        nav.navigate("todoList/${list.title}/${list.id}")
    }

    fun isNextChar(curChar: Char, prevChar: Char): Boolean {
        return if(curChar in '!' .. '/' && prevChar in '!' .. '/') false
        else if(curChar in '0' .. '9' && prevChar in '0' .. '9') false
        else curChar > prevChar
    }

    fun getSymbol(char:Char): String {
        return when (char) {
            in '!' .. '/' -> {
                "!#%"
            }
            in '0'..'9' -> {
                "0-9"
            }
            else -> char.toString()
        }
    }

    fun addClicked() {
        _addingNewList.value = true
    }
}

sealed class HomeUIState {
    data object Empty : HomeUIState()
    data object Loading : HomeUIState()
    data class Data(val favorites: List<CheckList>, val lists: List<CheckList>, val todos: List<ToDo>) : HomeUIState()
}
