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
    suspend fun insert(list: CheckList): Long

    //@Insert
    //suspend fun insert(list: List<CheckList>)

    @Update
    suspend fun update(list: CheckList)

    @Delete
    suspend fun delete(list: CheckList)

    @Query("SELECT * FROM CheckList")
    fun getAll(): Flow<List<CheckList>>

    @Query("SELECT * FROM CheckList ORDER BY Checklist.created DESC")
    fun getAllSortedByCreated(): Flow<List<CheckList>>

    @Query("SELECT * FROM CheckList ORDER BY Checklist.lastModified DESC")
    fun getAllSortedByLastModified(): Flow<List<CheckList>>

    @Query("SELECT * FROM CheckList ORDER BY Checklist.title COLLATE NOCASE ASC")
    fun getAllSortedByName(): Flow<List<CheckList>>

    @Query("SELECT * FROM CheckList WHERE CheckList.id = :vId")
    fun getWithId(vId: Int): Flow<CheckList>

    @Query("SELECT * FROM CheckList WHERE description LIKE '%' || :vSearchWord || '%'")
    fun findWithDescription(vSearchWord: String): Flow<List<CheckList>>

    @Query("SELECT * FROM CheckList WHERE title LIKE '%' || :vSearchWord || '%'")
    fun findWithTitle(vSearchWord: String): Flow<List<CheckList>>

    // Update name of list with id
    @Query("UPDATE CheckList SET title = :vNewTitle WHERE id = :vId")
    suspend fun updateTitle(vId: Int, vNewTitle: String)

    @Query("DELETE FROM CheckList WHERE id = :vId")
    suspend fun deleteWithId(vId: Int)

    @Query(
        """
        SELECT DISTINCT CheckList.* 
        FROM CheckList 
        JOIN (SELECT * FROM ToDo WHERE title LIKE '%' || :vSearchWord || '%') AS td 
        ON CheckList.id=td.listId 
        GROUP BY CheckList.title
        """
    )
    fun findWithTodosTitle(vSearchWord: String): Flow<List<CheckList>>

    @Query(
        """
        SELECT CAST(SUBSTR(title, LENGTH('New List ') + 1) AS INTEGER) + 1 AS listNumber
        FROM CheckList 
        WHERE title LIKE 'New List %' 
        ORDER BY listNumber 
        DESC LIMIT 1
        """
    )
    fun getNewListNr(): Int
}