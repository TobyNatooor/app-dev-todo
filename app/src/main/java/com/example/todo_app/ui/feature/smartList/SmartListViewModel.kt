package com.example.todo_app.ui.feature.smartList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.todo_app.MyApplication
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.data.Repository.UserRepository
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

class SmartListViewModel(
    db: AppDatabase,
    private val userRepository: UserRepository,
    private val nav: NavController
) : BaseViewModel(db) {

    companion object {
        fun createFactory(db: AppDatabase, navController: NavController): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = (this[APPLICATION_KEY] as MyApplication)
                    SmartListViewModel(db, application.userRepository, navController)
                }
            }
        }
    }

    val smartSettings = SmartSettingsSingleton.settings
    private val toDos: Flow<List<ToDo>> = db.toDoDao().getAll()

    private val filteredList = combine(
        smartSettings,
        toDos
    ) { settings, list ->
        list.filter { toDo ->
            includeOnStatus(toDo.status, settings)
        }
    }


    val _mutableToDosState = MutableStateFlow<ToDosUIState>(ToDosUIState.Loading)
    val toDosState: StateFlow<ToDosUIState> = _mutableToDosState.asStateFlow()

    init {
        viewModelScope.launch {
            filteredList.collect { list ->
                _mutableToDosState.value = ToDosUIState.Data(list)
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

    private fun includeOnStatus(status: ToDoStatus, smartSettings: SmartSettings): Boolean {
        return if (status == ToDoStatus.DONE && smartSettings.includeDone) true
        else if (status == ToDoStatus.NOT_DONE && smartSettings.includeNotDone) true
        else if (status == ToDoStatus.CANCELED && smartSettings.includeCancelled) true
        else if (status == ToDoStatus.IN_PROGRESS && smartSettings.includeInProgress) true
        else false
    }
}

sealed class ToDosUIState {
    data object Loading : ToDosUIState()
    data class Data(val toDos: List<ToDo>) : ToDosUIState()
}
