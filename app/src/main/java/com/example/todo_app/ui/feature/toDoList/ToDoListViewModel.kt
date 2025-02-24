package com.example.todo_app.ui.feature.toDoList

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.SortOption
import com.example.todo_app.model.ToDo
import com.example.todo_app.repository.CheckListRepositoryImpl
import com.example.todo_app.repository.ChecklistRepository
import com.example.todo_app.ui.feature.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val listId: Int,
    private val nav: NavController
) : BaseViewModel() {
    private val listRepo: ChecklistRepository = CheckListRepositoryImpl.getInstance()

    override val toDos: Flow<List<ToDo>> = toDoRepo.getAllWithListId(listId)

    private val _sortingOption = MutableStateFlow(SortOption.STATUS)
    val sortedOption: StateFlow<SortOption> = _sortingOption.asStateFlow()

    private val sortedLists: Flow<List<ToDo>> = combine(
        toDos,
        _sortingOption,
    ) { lists, sortOption ->

        when (sortOption) {
            SortOption.CREATED -> lists.sortedByDescending { it.created }
            SortOption.NAME -> lists.sortedBy { it.title.lowercase() }
            SortOption.RECENT -> lists.sortedByDescending { it.lastModified }
            SortOption.STATUS -> lists.sortedBy { it.status }
        }
    }

    val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState

    private val _addingNewToDo = MutableStateFlow(false)
    val addingNewToDo = _addingNewToDo.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()


    init {
        observeFavoriteStatus()
        viewModelScope.launch {
            sortedLists.collect { list ->
                _mutableToDosState.value = ToDosUIState.Data(list)
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
            toDoRepo.insert(newToDo)
            _addingNewToDo.value = false
        }
    }

    fun sortToDos(sortBy: SortOption) {
        _sortingOption.value = sortBy
    }

    fun clickToDoOptions(toDoId: Int) {
        nav.navigate("toDoOptions/${toDoId}")
    }

    fun updateList(listId: Int, newTitle: String) {
        viewModelScope.launch {
            listRepo.updateTitle(listId, newTitle)
        }
    }

    fun favoriteClicked(){
        this.viewModelScope.launch {
            listRepo.updateFavorite(listId, !isFavorite.value)
        }
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            listRepo.isFavorite(listId)
                .collect { favorite ->
                    _isFavorite.value = favorite
                }
        }
    }

    fun deleteList(listId: Int) {
        viewModelScope.launch {
            listRepo.deleteWithId(listId)
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
