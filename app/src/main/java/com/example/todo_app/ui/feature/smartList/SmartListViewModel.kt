package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.CheckList
import com.example.todo_app.ui.feature.BaseViewModel
import com.example.todo_app.model.ToDo
import com.example.todo_app.model.SmartSettings
import com.example.todo_app.model.SmartSettingsSingleton
import com.example.todo_app.model.ToDoStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Duration

class SmartListViewModel(
    db: AppDatabase,
    private val nav: NavController
) : BaseViewModel(db) {

    val smartSettings = SmartSettingsSingleton.settings
    private val toDos: Flow<List<ToDo>> = db.toDoDao().getAll()

    private val filteredList = combine(
        smartSettings,
        toDos
    ) { settings, list ->
        list.filter { toDo ->
            filter(toDo, settings)
        }
    }


    val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
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
            db.checkListDao().getAll().collect { list ->
                _checkListsState.value = list
            }
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

//    fun getSettings(): SmartSettings{
//        return smartSettings.value
//    }

    fun setSettings(settings: SmartSettings){
        SmartSettingsSingleton.updateSettings(settings)
    }

    /*override fun deleteToDo(toDo: ToDo) {
        viewModelScope.launch {
            db.toDoDao().delete(toDo)
        }
    }*/

    fun getCheckLists(): StateFlow<List<CheckList>> {
        return checkListsState
    }

    private fun filter(toDo: ToDo, smartSettings: SmartSettings): Boolean {
        val timeNow = LocalDateTime.now()
        return if (toDo.status == ToDoStatus.DONE && smartSettings.includeDone && (smartSettings.listId == null || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else if (toDo.status == ToDoStatus.NOT_DONE && smartSettings.includeNotDone && (smartSettings.listId == null || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else if (toDo.status == ToDoStatus.CANCELED && smartSettings.includeCancelled && (smartSettings.listId == null || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
        else if (toDo.status == ToDoStatus.IN_PROGRESS && smartSettings.includeInProgress && (smartSettings.listId == null || smartSettings.listId == toDo.listId) && (smartSettings.deadlineWithinDays == 0 || (toDo.deadline != null && getDifferenceInHours(timeNow, toDo.deadline) <= smartSettings.deadlineWithinDays * 24))) true
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
