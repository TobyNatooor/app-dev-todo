package com.example.todo_app.ui.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.todo_app.data.AppDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.SortOption
import com.example.todo_app.ui.theme.list
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HomeViewModel(private val db: AppDatabase, private val nav: NavController) : ViewModel() {
    var sortedOption : SortOption = SortOption.NAME
    private var lists: Flow<List<CheckList>> = listBySort(sortedOption)
    private val _mutableHomeState = MutableStateFlow<HomeUIState>(HomeUIState.Loading)
    val homeState: StateFlow<HomeUIState> = _mutableHomeState
    var currentChar: Char = '\u0000'

    init {
        viewModelScope.launch {
            lists = listBySort(sortedOption)
            lists.collect { list ->
                _mutableHomeState.value = if (list.isEmpty()) {
                    HomeUIState.Empty
                } else {
                    println("viewModel launched - sorting option is '${sortedOption}'")
                    HomeUIState.Data(list)
                }
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

    fun sortLists(sortBy: SortOption){
        sortedOption = sortBy
        this.viewModelScope.launch {
            lists = listBySort(sortBy)
            lists.collect { list -> _mutableHomeState.value = HomeUIState.Data(list)}
        }
    }

    fun updateList(list: CheckList){
        this.viewModelScope.launch {
            db.checkListDao().update(list)
        }
    }

    fun clickList(list: CheckList){
        this.viewModelScope.launch {
            db.checkListDao().update(list.copy(lastModified = LocalDateTime.now()))
        }
        nav.navigate("todoList/${list.title}/${list.id}")
    }

    private fun listBySort(sortBy: SortOption): Flow<List<CheckList>>{
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
    data class Data(val lists: List<CheckList>) : HomeUIState()
}
