package com.example.todo_app.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo_app.model.Folder
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Insert
    suspend fun insert(folder: Folder)

    @Update
    suspend fun update(folder: Folder)

    @Delete
    suspend fun delete(folder: Folder)

    @Query("SELECT * FROM Folder")
    fun getAll(): Flow<List<Folder>>

    @Query("SELECT * FROM Folder WHERE Folder.parentId = :vParentId")
    fun getAllWithParentId(vParentId: Int): Flow<List<Folder>>

    @Query("SELECT * FROM Folder WHERE Folder.id = :vId")
    fun getWithId(vId: Int): Flow<Folder>

    @Query("SELECT * FROM Folder WHERE title LIKE '%' || :vSearchWord || '%'")
    fun findWithTitle(vSearchWord: String): Flow<List<Folder>>
}