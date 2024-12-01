package com.example.todo_app.ui.feature.home

import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(val db: AppDatabase, val nav: NavController) : ViewModel() {

    private val lists: Flow<List<CheckList>> = db.checkListDao().getAll()
    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState

    init {
        viewModelScope.launch {
            lists.collect { list ->
                _mutableHomeState.value = if (list.isEmpty()) {
                    HomeUIState.Empty
                } else {
                    HomeUIState.Data(list)
                }
            }

        }
    }

    suspend fun addList() {
        val newList = CheckList(
            title = "New List",
            description = "Add Description",
            order = 2, //TODO: Add query to find max order
            folderId = 0
        )
        db.checkListDao().insert(newList)
    }

    fun clickList(listTitle: String, listId: Int){
        nav.navigate("todoList/${listTitle}/${listId}")
    }
}

sealed class HomeUIState {
    data object Empty : HomeUIState()
    data object Loading : HomeUIState()
    data class Data(val lists: List<CheckList>) : HomeUIState()
}
