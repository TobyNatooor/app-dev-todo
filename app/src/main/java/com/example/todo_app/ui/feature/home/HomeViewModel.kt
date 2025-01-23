package com.example.todo_app.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.SortOption
import com.example.todo_app.model.ToDo
import com.example.todo_app.repository.CheckListRepositoryImpl
import com.example.todo_app.repository.ChecklistRepository
import com.example.todo_app.repository.ToDoRepoImpl
import com.example.todo_app.repository.ToDoRepository
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

class HomeViewModel(private val nav: NavController) : ViewModel() {
    private val listRepo: ChecklistRepository = CheckListRepositoryImpl.getInstance()
    private val toDoRepo: ToDoRepository = ToDoRepoImpl.getInstance()

    private val _sortingOption = MutableStateFlow(SortOption.NAME)
    val sortedOption: StateFlow<SortOption> = _sortingOption.asStateFlow()

    private val favoriteLists: Flow<List<CheckList>> = listRepo.getAllFavorites()

    private val _filterQuery = MutableStateFlow("")
    val filteringQuery = _filterQuery.asStateFlow()

    private val filteredLists: Flow<List<CheckList>> = filteringQuery.flatMapLatest { query ->
        if (query.isBlank()) listRepo.getAllNonFavorite()
        else listRepo.findWithToDosTitle(query)
    }

    private val sortedLists: Flow<List<CheckList>> = combine(
        filteredLists,
        _sortingOption,
    ) { lists, sortOption ->
        delay(100)

        when (sortOption) {
            SortOption.CREATED -> lists.sortedByDescending { it.created }
            SortOption.NAME -> lists.sortedBy { it.title.lowercase() }
            SortOption.RECENT -> lists.sortedByDescending { it.lastModified }
            else -> lists
        }
    }
    private val todos: Flow<List<ToDo>> =  filteringQuery.flatMapLatest { query ->
        if (query.isBlank()) flowOf(ArrayList())
        else toDoRepo.findWithTitle(query)
    }

    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState.asStateFlow()

    private val _mutableNewList = MutableStateFlow<NewListState>(NewListState.Empty)
    val newListState: StateFlow<NewListState> = _mutableNewList.asStateFlow()

    init {
        viewModelScope.launch {
            combine(favoriteLists, sortedLists, todos) { favorites, lists, todos ->
                if (lists.isEmpty()) {
                    HomeUIState.Empty
                } else {
                    HomeUIState.Data(favorites, lists, todos)
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

    fun initializeNewList() {
        this.viewModelScope.launch {
            _mutableNewList.value = NewListState.Data(CheckList(
                title = "New list",
                description = ""
            ))
        }
    }

    fun addNewList(title: String) {
        viewModelScope.launch {
            val currentNewListState = _mutableNewList.value
            _mutableNewList.value = NewListState.Empty

            if (currentNewListState is NewListState.Data) {
                listRepo.insert(currentNewListState.list.copy(title = title))
            }
        }
    }

    fun sortLists(sortBy: SortOption) {
        _sortingOption.value = sortBy
    }

    fun updateList(list: CheckList) {
        this.viewModelScope.launch {
            listRepo.update(list)
        }
    }

    fun deleteList(listId: Int) {
        this.viewModelScope.launch {
            listRepo.deleteWithId(listId)
        }
    }

    fun clickList(list: CheckList) {
        this.viewModelScope.launch {
            listRepo.update(list.copy(lastModified = LocalDateTime.now()))
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

    fun clickedSmartList() {
        nav.navigate("smartList")
    }
}

sealed class HomeUIState {
    data object Empty : HomeUIState()
    data object Loading : HomeUIState()
    data class Data(val favorites: List<CheckList>, val lists: List<CheckList>, val todos: List<ToDo>) : HomeUIState()
}

sealed class NewListState {
    data object Empty : NewListState()
    data class Data(val list: CheckList) : NewListState()
}
