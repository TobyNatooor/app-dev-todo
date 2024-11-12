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
                    description = "Lorem ipsum"
                ),
                ToDo(
                    title = "To Do 1.2",
                    description = "In facilisis volutpat"
                )
            ),
            description = "Donec venenatis est a est"
        ),
        CheckList(
            title = "List 2",
            arrayOf(
                ToDo(
                    title = "To Do 2.1",
                    description = "Sed aliquet risus a tortor"
                ),
                ToDo(
                    title = "To Do 2.2",
                    description = "Curabitur pharetra"
                ),
                ToDo(
                    title = "To Do 2.3",
                    description = "Curabitur pharetra"
                ),
                ToDo(
                    title = "To Do 2.4",
                    description = "Curabitur pharetra"
                ),
                ToDo(
                    title = "To Do 2.5",
                    description = "Curabitur pharetra"
                ),
                ToDo(
                    title = "To Do 2.6",
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
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 3.2",
                    description = "Vivamus sagittis lacus"
                ),
                ToDo(
                    title = "To Do 3.3",
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
                    description = "Nullam dictum felis eu"
                ),
                ToDo(
                    title = "To Do 4.2",
                    description = "Nam quam nunc, blandit vel"
                ),
                ToDo(
                    title = "To Do 4.3",
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
                    description = "Curabitur ullamcorper ultricies"
                ),
                ToDo(
                    title = "To Do 5.2",
                    description = "Etiam sit amet orci eget"
                ),
                ToDo(
                    title = "To Do 5.3",
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
                    description = "Donec quam felis, ultricies"
                ),
                ToDo(
                    title = "To Do 6.2",
                    description = "Vestibulum fringilla pede sit"
                ),
                ToDo(
                    title = "To Do 6.3",
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
                    description = "Integer tincidunt"
                ),
                ToDo(
                    title = "To Do 7.2",
                    description = "Nullam quis ante"
                ),
                ToDo(
                    title = "To Do 7.3",
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
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 8.2",
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 8.3",
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        ),
        CheckList(
            title = "List 9",
            arrayOf(
                ToDo(
                    title = "To Do 9.1",
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 9.2",
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 9.3",
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        ),
        CheckList(
            title = "List 10",
            arrayOf(
                ToDo(
                    title = "To Do 10.1",
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 10.2",
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 10.3",
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        ),
        CheckList(
            title = "List 11",
            arrayOf(
                ToDo(
                    title = "To Do 11.1",
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 11.2",
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 11.3",
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        ),
        CheckList(
            title = "List 12",
            arrayOf(
                ToDo(
                    title = "To Do 12.1",
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 12.2",
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 12.3",
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        ),
        CheckList(
            title = "List 13",
            arrayOf(
                ToDo(
                    title = "To Do 13.1",
                    description = "Vestibulum ante ipsum"
                ),
                ToDo(
                    title = "To Do 13.2",
                    description = "Fusce pretium"
                ),
                ToDo(
                    title = "To Do 13.3",
                    description = "Curabitur at lacus ac"
                )
            ),
            description = "Nam eget dui"
        )
    );

    fun getLists(): List<CheckList> {
        return lists;
    }
}