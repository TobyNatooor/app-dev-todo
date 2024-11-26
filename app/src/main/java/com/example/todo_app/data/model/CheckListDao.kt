package com.example.todo_app.data.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo_app.model.CheckList

@Dao
interface CheckListDao {
    @Insert
    suspend fun insert(list: CheckList)

    @Update
    suspend fun update(list: CheckList)

    @Delete
    suspend fun delete(list: CheckList)

    @Query("SELECT * FROM CheckList")
    fun getAllLists(): LiveData<List<CheckList>>

}