package com.example.todo_app.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo_app.model.ToDo
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao {
    @Insert
    suspend fun insert(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Delete
    suspend fun delete(toDo: ToDo)

    @Query("SELECT * FROM ToDo")
    fun getAll(): Flow<List<ToDo>>

    @Query("SELECT * FROM ToDo WHERE ToDo.id = :vId")
    fun getWithId(vId: Int): Flow<ToDo>

    @Query("SELECT * FROM ToDo WHERE ToDo.listId = :vListId")
    fun getAllWithListId(vListId: Int): Flow<List<ToDo>>

    @Query("SELECT * FROM ToDo WHERE description LIKE '%' || :vSearchWord || '%'")
    fun findWithDescription(vSearchWord: String): Flow<List<ToDo>>

    @Query("SELECT * FROM ToDo WHERE title LIKE '%' || :vSearchWord || '%'")
    fun findWithTitle(vSearchWord: String): Flow<List<ToDo>>

    @Query("DELETE FROM ToDo WHERE TRIM(title) = '' OR title IS NULL")
    fun deleteEmptyToDos()

    @Query("SELECT COUNT(*) FROM ToDo")
    fun numberOfToDos(): Int

}