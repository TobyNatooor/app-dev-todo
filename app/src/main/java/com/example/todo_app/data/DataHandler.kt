package com.example.todo_app.data

import android.os.Environment
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo

class DataHandler {
    val folderpath = Environment.getExternalStorageDirectory().getPath()
    val mockLists = MockCheckListDataStore();
    val mockTodos = MockToDoDataStore();

    fun save(lists: HashMap<Int, CheckList>){
        // val listsToJson = Json.encodeToString(lists)
        // try {
        //     val file = File(folderpath + "todo_data.json")
        //     file.writeText(listsToJson)
        // } catch (e: Exception){
        //     e.printStackTrace()
        // }
        var newLists = emptyList<CheckListDOA>().toMutableList()
        var newToDos = emptyList<ToDoDOA>().toMutableList()

        for(list in lists){
            newLists.add(
                list.key,
                CheckListDOA(
                    id = list.key,
                    title = list.value.title,
                    description = list.value.description
                )
            )
            for(toDo in list.value.toDos){
                newToDos.add(
                    toDo.key,
                    ToDoDOA(
                        id = toDo.key,
                        title = toDo.value.title,
                        isDone = toDo.value.isDone,
                        description = toDo.value.description,
                        listId = list.key
                    )
                )
            }
        }

        mockLists.checklistData = newLists
        mockTodos.todoData = newToDos
    }

    fun load(): HashMap<Int, CheckList>{
        // var lists: List<CheckList> = emptyList();
        // try {
        //     val file = File(folderpath + "todo_data.json")
        //     val listsToJson = file.readText()
        //     lists = Json.decodeFromString(listsToJson)
        // } catch (e: Exception){
        //     e.printStackTrace()
        // }

        val doaList = mockLists.checklistData
        val doaTodos = mockTodos.todoData

        val lists = HashMap<Int, CheckList>()

        for (list in doaList){
            lists.put(
                list.id, 
                CheckList(
                    title = list.title,
                    toDos = HashMap<Int, ToDo>(),
                    description = list.description,
                )
            )
        }

        for (toDo in doaTodos ){
            if(lists.containsKey(toDo.listId)) {
                lists.get(toDo.listId)?.toDos?.put(
                    toDo.id,
                    ToDo(
                        title = toDo.title,
                        isDone = toDo.isDone,
                        description = toDo.description
                    )
                )
            }
        }


        return lists;
    }

}