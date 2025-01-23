package com.example.todo_app.repository

import com.example.todo_app.data.model.CheckListDao
import com.example.todo_app.model.CheckList
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getAll(): Flow<List<CheckList>>

    suspend fun getWithId(id: Int): CheckList

    fun isFavorite(id: Int): Flow<Boolean>

    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    suspend fun updateTitle(id: Int, newTitle: String)

    suspend fun deleteWithId(id: Int)
}

class CheckListRepositoryImpl(private val listDao: CheckListDao): ChecklistRepository {
    override fun getAll(): Flow<List<CheckList>> {
        return listDao.getAll()
    }

    override suspend fun getWithId(id: Int): CheckList {
        return listDao.getWithId(id)
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return listDao.isFavorite(id)
    }

    override suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        listDao.updateFavorite(id, isFavorite)
    }

    override suspend fun updateTitle(id: Int, newTitle: String) {
        listDao.updateTitle(id, newTitle)
    }

    override suspend fun deleteWithId(id: Int) {
        listDao.deleteWithId(id)
    }
}