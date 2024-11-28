package com.example.todo_app.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo_app.model.CheckList
import kotlinx.coroutines.flow.Flow

@Dao
interface CheckListDao {
    @Insert
    suspend fun insert(list: CheckList)

    @Update
    suspend fun update(list: CheckList)

    @Delete
    suspend fun delete(list: CheckList)

    @Query("SELECT * FROM CheckList")
    fun getAll(): Flow<List<CheckList>>

    @Query("SELECT CAST(SUBSTR(title, LENGTH('New List ') + 1) AS INTEGER) AS listNumber " +
            "FROM CheckList WHERE title LIKE 'New List %' ORDER BY listNumber DESC LIMIT 1")
    fun getNewListNr(): Int
}