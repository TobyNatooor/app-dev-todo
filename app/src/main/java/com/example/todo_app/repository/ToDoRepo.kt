package com.example.todo_app.repository

import com.example.todo_app.data.DataHandler
import com.example.todo_app.data.mock.MockToDoDataStore
import com.example.todo_app.model.ToDo

class ToDoRepo {
    var dataSource = DataHandler().toDoDataSource;

    fun getToDos(listId: Int): List<ToDo> {
        val toDos = emptyList<ToDo>().toMutableList()
        for (toDoDAO in dataSource.todoData){
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

    private fun update(){
        dataSource = DataHandler().toDoDataSource
    }
}