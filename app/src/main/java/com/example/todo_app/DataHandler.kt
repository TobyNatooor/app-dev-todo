package com.example.todo_app

import android.os.Environment
import com.example.todo_app.model.List
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import android.content.Context

class DataHandler {
    val folderpath = Environment.getExternalStorageDirectory().getPath()

    fun save(lists: Array<List>){
        val listsToJson = Json.encodeToString(lists)
        try {
            val file = File(folderpath + "todo_data.json")
            file.writeText(listsToJson)
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun load(): Array<List>{
        var lists: Array<List> = emptyArray()
        try {
            val file = File(folderpath + "todo_data.json")
            val listsToJson = file.readText()
            lists = Json.decodeFromString(listsToJson)
        } catch (e: Exception){
            e.printStackTrace()
        }
        return lists;
    }

}