package com.example.todo_app.repository

import com.example.todo_app.data.model.ToDoDao
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getWithId(id: Int): Flow<ToDo>
    fun getAllWithListId(listId: Int): Flow<List<ToDo>>
    fun getAll(): Flow<List<ToDo>>
    fun findWithTitle(query: String): Flow<List<ToDo>>
    suspend fun insert(toDo: ToDo)
    suspend fun update(toDo: ToDo)
    suspend fun delete(toDo: ToDo)
}

class ToDoRepoImpl(private val toDoDao: ToDoDao): ToDoRepository {
    override fun getWithId(id: Int): Flow<ToDo> {
        return toDoDao.getWithId(id)
    }

    override fun getAllWithListId(listId: Int): Flow<List<ToDo>> {
        return toDoDao.getAllWithListId(listId)
    }

    override fun getAll(): Flow<List<ToDo>> {
        return toDoDao.getAll()
    }

    override fun findWithTitle(query: String): Flow<List<ToDo>> {
        return toDoDao.findWithTitle(query)
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
