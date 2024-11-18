package com.example.todo_app.data

import android.os.Environment
import com.example.todo_app.data.mock.MockCheckListDataStore
import com.example.todo_app.data.mock.MockToDoDataStore
import com.example.todo_app.data.model.CheckListDAO
import com.example.todo_app.data.model.ToDoDAO
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class DataHandler {
    val folderpath = Environment.getExternalStorageDirectory().getPath()

    val checkListDataStore = MockCheckListDataStore();
    val toDoDataSource = MockToDoDataStore();

    /*
    ============ GET FOR CHECKLIST AND TODOS ============
     */

    fun getCheckLists(): List<CheckList> {
        val lists = emptyList<CheckList>().toMutableList()
        for (listDAO in checkListDataStore.checklistData){
            lists.add(
                CheckList(
                    id = listDAO.id,
                    title = listDAO.title,
                    description = listDAO.description
                )
            )
        }

        return lists
    }

    fun getToDos(listId: Int): List<ToDo> {
        val toDos = emptyList<ToDo>().toMutableList()
        for (toDoDAO in toDoDataSource.todoData){
            if(toDoDAO.listId == listId){
                toDos.add(
                    ToDo(
                        id = toDoDAO.id,
                        title = toDoDAO.title,
                        isDone = toDoDAO.isDone,
                        description = toDoDAO.description
                    )
                )
            }
        }
        return toDos
    }

    /*
    ============ SAVE FOR CHECKLIST AND TODOS ============
     */

    fun save(toDo : ToDo, listId: Int){
        for (toDoDAO in toDoDataSource.todoData){
            if(toDo.id == toDoDAO.id){
                toDoDAO.overrideWith(toDo)
                return
            }
        }
        toDoDataSource.todoData.add(
            ToDoDAO(
                id = toDo.id,
                title = toDo.title,
                isDone = toDo.isDone,
                description = toDo.description,
                listId = listId
            ))
    }

    fun save(toDos : List<ToDo>, listId: Int){
        for(toDo in toDos){
            save(toDo, listId)
        }
    }

    fun save(list : CheckList){
        for(listDAO in checkListDataStore.checklistData){
            if(listDAO.id == list.id){
                listDAO.overrideWith(list)
                return
            }
        }
        checkListDataStore.checklistData.add(
            CheckListDAO(
                id = list.id,
                title = list.title,
                description = list.description
            )
        )
    }

    fun save(lists: List<CheckList>){
        for (list in lists){
            save(list)
        }
    }

    fun load(): List<CheckList>{
        var lists: List<CheckList> = emptyList();
        try {
            val file = File(folderpath + "todo_data.json")
            val listsToJson = file.readText()
            lists = Json.decodeFromString(listsToJson)
        } catch (e: Exception){
            e.printStackTrace()
        }
        return lists;
    }

    fun createNewListName(lists: List<CheckList>): String {
        var i = 1
        while (lists.any { it.title == "new list $i" }) {
            i++
        }
        return "new list $i"
    }

    fun newToDoId() : Int {
        var newId = 0
        for(toDo in toDoDataSource.todoData){
            if(toDo.id > newId){
                newId = toDo.id
            }
        }
        newId++
        return newId
    }

}