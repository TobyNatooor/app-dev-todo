package com.example.todo_app.repository

import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getToDoById(id: Int): Flow<ToDo>
    fun getAllWithListId(listId: Int): Flow<List<ToDo>>
    fun getAll(): Flow<List<ToDo>>
    suspend fun insert(toDo: ToDo)
    suspend fun update(toDo: ToDo)
    suspend fun delete(toDo: ToDo)
}