package com.example.todo_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todo_app.data.model.CheckListDao
import com.example.todo_app.data.model.FolderDao

import com.example.todo_app.data.model.ToDoDao
import com.example.todo_app.data.util.Converters
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.Folder
import com.example.todo_app.model.ToDo

@Database(entities = [ToDo::class, CheckList::class, Folder::class/*SmartSettings::class */], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoDao(): ToDoDao
    abstract fun checkListDao(): CheckListDao
    abstract fun folderDao(): FolderDao
    //abstract fun smartSettingsDao(): SmartSettingsDao
}
