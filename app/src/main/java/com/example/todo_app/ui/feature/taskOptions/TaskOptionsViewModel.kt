package com.example.todo_app.ui.feature.taskOptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.ToDo
import com.example.todo_app.model.ToDoStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskOptionsViewModel(private val taskId: Int, private val db: AppDatabase) : ViewModel() {
    private val _mutableTaskState = MutableStateFlow<TaskUIState>(TaskUIState.Loading)
    val taskState: StateFlow<TaskUIState> = _mutableTaskState

    init {
        viewModelScope.launch {
            val taskFlow: Flow<ToDo> = db.toDoDao().getWithId(taskId)
            taskFlow.collect { task ->
                _mutableTaskState.value = TaskUIState.Data(task)
            }
        }
    }

    fun updateTask(updatedTask: ToDo) {
        val taskToUpdate = if (updatedTask.title.isNullOrBlank()) {
            updatedTask.copy(title = "Unnamed to do item")
        } else {
            updatedTask
        }

        viewModelScope.launch {
            db.toDoDao().update(taskToUpdate)
        }
    }
}

sealed class TaskUIState {
    data object Loading : TaskUIState()
    data class Data(val task: ToDo) : TaskUIState()
}
