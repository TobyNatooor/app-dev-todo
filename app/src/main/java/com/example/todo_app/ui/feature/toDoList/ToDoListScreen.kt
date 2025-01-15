package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme
import com.example.todo_app.ui.feature.common.*

@Composable
fun ToDoListScreen(
    modifier: Modifier = Modifier,
    title: String = "",
    listId: Int,
    appBar: @Composable () -> Unit,
    db: AppDatabase,
    navController: NavController
) {
    val viewmodel: ToDoListViewModel = viewModel(
        key = "ToDoListViewModel_$listId",
        factory = ToDoListViewModelFactory(listId, db, navController)
    )
    val toDosUIState = viewmodel.toDosState.collectAsState().value
    val focusManager = LocalFocusManager.current

    TodoappTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                },
            floatingActionButton = {
                AddButton(onClick = {
                    viewmodel.addToDoItem()
                })
            },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                //appBar()
                Box(modifier = modifier) {
                    when (toDosUIState) {
                        is ToDosUIState.Loading -> LoadingScreen(
                            modifier = modifier
                        )

                        is ToDosUIState.Data -> ToDoList(
                            title = title,
                            listId = listId,
                            toDos = toDosUIState.toDos,
                            viewmodel = viewmodel,
                            modifier = modifier,
                            appBar = appBar
                        )
                    }
                }
            }
        }
    }
}
