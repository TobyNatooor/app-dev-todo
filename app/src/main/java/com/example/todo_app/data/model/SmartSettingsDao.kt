package com.example.todo_app.data.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todo_app.model.SmartSettings
import kotlinx.coroutines.flow.Flow

@Dao
interface SmartListDao {

    @Update
    suspend fun update()

    @Delete
    suspend fun delete()
}