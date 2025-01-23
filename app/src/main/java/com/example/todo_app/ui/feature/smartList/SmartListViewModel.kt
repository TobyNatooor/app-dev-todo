package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import com.example.todo_app.MyApplication
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.repository.UserRepository
import com.example.todo_app.model.CheckList
import com.example.todo_app.ui.feature.BaseViewModel
import com.example.todo_app.model.ToDo
import com.example.todo_app.model.SmartSettings
import com.example.todo_app.model.ToDoStatus
import com.example.todo_app.repository.ChecklistRepository
import com.example.todo_app.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Duration

class SmartListViewModel(
    toDoRepo: ToDoRepository,
    listRepo: ChecklistRepository,
    private val userRepository: UserRepository,
    private val nav: NavController
) : BaseViewModel(toDoRepo) {

    companion object {
        fun createFactory(toDoRepo: ToDoRepository, listRepo: ChecklistRepository, navController: NavController): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as MyApplication)
                    SmartListViewModel(toDoRepo, listRepo, application.userRepository, navController)
                }
            }
        }
    }

    val smartSettings = userRepository.smartSettings

    private val toDos: Flow<List<ToDo>> = toDoRepo.getAll()

    private val filteredList = combine(
        smartSettings,
        toDos
    ) { settings, list ->
        list.filter { toDo ->
            filter(toDo, settings)
        }
    }


    private val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState.asStateFlow()

    private val _checkListsState = MutableStateFlow<List<CheckList>>(emptyList())
    val checkListsState: StateFlow<List<CheckList>> = _checkListsState.asStateFlow()

    init {
        viewModelScope.launch {
            filteredList.collect { list ->
                _mutableToDosState.value = ToDosUIState.Data(list)
            }
        }
        viewModelScope.launch {
            listRepo.getAll().collect { list ->
                _checkListsState.value = list
            }
        }
    }

    fun clickToDoOptions(toDoId: Int) {
        nav.navigate("toDoOptions/${toDoId}")
    }


    fun setSettings(settings: SmartSettings){
        this.viewModelScope.launch {
            userRepository.updateSettings(settings)
        }
    }

    fun getCheckLists(): StateFlow<List<CheckList>> {
        return checkListsState
    }

    private fun filter(toDo: ToDo, smartSettings: SmartSettings): Boolean {
        val timeNow = LocalDateTime.now()
        return if (toDo.status == ToDoStatus.DONE && smartSettings.includeDone && (smartSettings.listId == -1 || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else if (toDo.status == ToDoStatus.NOT_DONE && smartSettings.includeNotDone && (smartSettings.listId == -1 || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else if (toDo.status == ToDoStatus.CANCELED && smartSettings.includeCancelled && (smartSettings.listId == -1 || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else if (toDo.status == ToDoStatus.IN_PROGRESS && smartSettings.includeInProgress && (smartSettings.listId == -1 || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else false
    }

    private fun getDifferenceInHours(start: LocalDateTime, end: LocalDateTime?): Int {
        return Duration.between(start, end).toHours().toInt()
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
