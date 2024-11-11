package com.example.todo_app.repository

import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import java.util.Date

class MockCheckListDataStore {

    private val lists = listOf(
        CheckList(
            title = "List 1",
            arrayOf(
                ToDo(
                    title = "To Do 1.1",
                    deadline = Date(10000),
                    description = "Lorem ipsum"),
                ToDo(
                    title = "To Do 1.2",
                    deadline = Date(20000),
                    description = "In facilisis volutpat"
                )
            ),
            description = "Donec venenatis est a est"),
        CheckList(
            title = "List 2",
            arrayOf(
                ToDo(
                    title = "To Do 2.1",
                    deadline = Date(30000),
                    description = "Sed aliquet risus a tortor"
                ),
                ToDo(
                    title = "To Do 2.2",
                    deadline = Date(40000),
                    description = "Curabitur pharetra"
                ),
                ToDo(
                    title = "To Do 2.3",
                    deadline = Date(50000),
                    description = "Mauris malesuada elit"
                )
            ),
            description = "Integer nec odio"
        ),
        CheckList(
            title = "List 3",
            arrayOf(
                ToDo(
                    title = "To Do 3.1",
                    deadline = Date(60000),
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 3.2",
                    deadline = Date(70000),
                    description = "Vivamus sagittis lacus"
                ),
                ToDo(
                    title = "To Do 3.3",
                    deadline = Date(80000),
                    description = "Donec congue lacinia dui"
                )
            ),
            description = "Etiam ultricies nisi"
        ),
        CheckList(
            title = "List 4",
            arrayOf(
                ToDo(
                    title = "To Do 4.1",
                    deadline = Date(90000),
                    description = "Nullam dictum felis eu"
                ),
                ToDo(
                    title = "To Do 4.2",
                    deadline = Date(100000),
                    description = "Nam quam nunc, blandit vel"
                ),
                ToDo(
                    title = "To Do 4.3",
                    deadline = Date(110000),
                    description = "Pellentesque habitant morbi"
                )
            ),
            description = "Fusce ac felis sit amet"
        ),
        CheckList(
            title = "List 5",
            arrayOf(
                ToDo(
                    title = "To Do 5.1",
                    deadline = Date(120000),
                    description = "Curabitur ullamcorper ultricies"
                ),
                ToDo(
                    title = "To Do 5.2",
                    deadline = Date(130000),
                    description = "Etiam sit amet orci eget"
                ),
                ToDo(
                    title = "To Do 5.3",
                    deadline = Date(140000),
                    description = "Fusce convallis metus"
                )
            ),
            description = "Aenean commodo ligula eget dolor"
        ),
        CheckList(
            title = "List 6",
            arrayOf(
                ToDo(
                    title = "To Do 6.1",
                    deadline = Date(150000),
                    description = "Donec quam felis, ultricies"
                ),
                ToDo(
                    title = "To Do 6.2",
                    deadline = Date(160000),
                    description = "Vestibulum fringilla pede sit"
                ),
                ToDo(
                    title = "To Do 6.3",
                    deadline = Date(170000),
                    description = "In ac felis quis tortor"
                )
            ),
            description = "Curabitur ullamcorper ultricies nisi"
        ),
        CheckList(
            title = "List 7",
            arrayOf(
                ToDo(
                    title = "To Do 7.1",
                    deadline = Date(180000),
                    description = "Integer tincidunt"
                ),
                ToDo(
                    title = "To Do 7.2",
                    deadline = Date(190000),
                    description = "Nullam quis ante"
                ),
                ToDo(
                    title = "To Do 7.3",
                    deadline = Date(200000),
                    description = "Phasellus viverra nulla ut"
                )
            ),
            description = "Aenean leo ligula"
        ),
        CheckList(
            title = "List 8",
            arrayOf(
                ToDo(
                    title = "To Do 8.1",
                    deadline = Date(210000),
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 8.2",
                    deadline = Date(220000),
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 8.3",
                    deadline = Date(230000),
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        )
    );

}