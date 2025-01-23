package com.example.todo_app.repository

import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.CheckList
import kotlinx.coroutines.flow.Flow

interface ChecklistRepository {
    fun getAll(): Flow<List<CheckList>>
    fun getAllFavorites(): Flow<List<CheckList>>
    fun getAllNonFavorite(): Flow<List<CheckList>>
    fun findWithToDosTitle(query: String): Flow<List<CheckList>>
    suspend fun getWithId(id: Int): CheckList
    fun isFavorite(id: Int): Flow<Boolean>
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)
    suspend fun updateTitle(id: Int, newTitle: String)
    suspend fun deleteWithId(id: Int)
    suspend fun insert(list: CheckList)
    suspend fun update(list: CheckList)
}

class CheckListRepositoryImpl: ChecklistRepository {
    private val listDao = AppDatabase.getDatabase().checkListDao()

    override fun getAll(): Flow<List<CheckList>> {
        return listDao.getAll()
    }

    override fun getAllFavorites(): Flow<List<CheckList>> {
        return listDao.getAllFavorites()
    }

    override fun getAllNonFavorite(): Flow<List<CheckList>> {
        return listDao.getAllNonFavorite()
    }

    override fun findWithToDosTitle(query: String): Flow<List<CheckList>> {
        return listDao.findWithTodosTitle(query)
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

    override suspend fun insert(list: CheckList) {
        listDao.insert(list)
    }

    override suspend fun update(list: CheckList) {
        listDao.update(list)
    }

    companion object {

        @Volatile private var instance: CheckListRepositoryImpl? = null // Volatile modifier is necessary

        fun getInstance() =
            instance ?: synchronized(this) { // synchronized to avoid concurrency problem
                instance ?: CheckListRepositoryImpl().also { instance = it }
            }
    }
}