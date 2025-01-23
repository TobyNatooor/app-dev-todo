package com.example.todo_app.repository

import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getToDoById(id: Int): Flow<ToDo>
    fun getAllToDo(): Flow<List<ToDo>>
    suspend fun addToDo(toDo: ToDo)
    suspend fun updateToDo(toDo: ToDo)
    suspend fun deleteToDo(toDo: ToDo)
}