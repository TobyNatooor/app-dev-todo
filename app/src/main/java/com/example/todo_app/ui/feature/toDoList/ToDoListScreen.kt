package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.clickable
import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.EmptyScreen
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme
import kotlinx.coroutines.launch


@Composable
fun ToDoListScreen(
    modifier: Modifier = Modifier,
    title: String = "", listId : Int,
    appBar : @Composable () -> Unit,
    db: AppDatabase
) {
    val viewmodel: ToDoListViewModel = viewModel(
        key = "ToDoListViewModel_$listId",
        factory = ToDoListViewModelFactory(listId, db)
    )
    val toDosUIState = viewmodel.toDosState.collectAsState().value
    val focusManager = LocalFocusManager.current

    TodoappTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    focusManager.clearFocus()
                },
            floatingActionButton = { AddButton(viewmodel) },
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                appBar()
                Box(modifier = modifier) {
                    ToDosContent(toDosUIState, viewmodel, title, focusManager)
                }
            }

        }
    }

}

@Composable
private fun ToDosContent(
    toDosUIState: ToDosUIState,
    viewmodel: ToDoListViewModel,
    title: String,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    when (toDosUIState) {
        is ToDosUIState.Empty -> EmptyScreen(
            modifier = modifier,
            title = title,
            text = "No to-do items in this list yet"
        )
        is ToDosUIState.Loading -> LoadingScreen(modifier)
        is ToDosUIState.Data -> ToDoList(
            modifier = modifier,
            title = title,
            toDos = toDosUIState.toDos,
            viewmodel = viewmodel
        )
    }
}

@Composable
fun AddButton(viewModel: ToDoListViewModel) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            coroutineScope.launch { viewModel.addToDoItem() }
        },
        // Remove shape parameter for default shape (square with rounded corners)
        shape = RoundedCornerShape(45, 45, 45, 45),
        modifier = Modifier.padding(20.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new to do", tint = Color.Black)
    }
}
