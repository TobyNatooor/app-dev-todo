package com.example.todo_app.repository

import com.example.todo_app.data.model.ToDoDao
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow

class ToDoRepoImpl(private val toDoDao: ToDoDao): ToDoRepository {
    override fun getToDoById(id: Int): Flow<ToDo> {
        return toDoDao.getWithId(id)
    }

    override fun getAllWithListId(listId: Int): Flow<List<ToDo>> {
        return toDoDao.getAllWithListId(listId)
    }

    override fun getAll(): Flow<List<ToDo>> {
        return toDoDao.getAll()
    }

    override suspend fun insert(toDo: ToDo) {
        toDoDao.insert(toDo)
    }

    override suspend fun update(toDo: ToDo) {
        toDoDao.update(toDo)
    }

    override suspend fun delete(toDo: ToDo) {
        toDoDao.delete(toDo)
    }
}