package com.example.todo_app.ui.feature.toDoList

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo_app.data.DataHandler
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.EmptyScreen
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme


@Composable
fun ToDoListScreen(
    modifier: Modifier = Modifier, title: String = "", listId : Int
) {
    val viewmodel = ToDoListViewModel(listId)
    val toDosUIState = viewmodel.toDosState.collectAsState().value

    TodoappTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = { AddButton(viewmodel) }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                AppBar()
                Box(modifier = modifier) {

                    ToDosContent(toDosUIState, viewmodel, title)

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
    modifier: Modifier = Modifier
) {

    when (toDosUIState) {
        ToDosUIState.Empty -> EmptyScreen("No to-do items in this list yet")
        is ToDosUIState.Loading -> LoadingScreen(modifier)
        is ToDosUIState.Data -> ToDoList(
            toDos = toDosUIState.toDos,
            viewmodel = viewmodel,
            modifier = modifier,
            title = title
        )
    }
}


@Composable
fun AppBar() {
    val activity = LocalContext.current as? Activity
    return Row(
        modifier = Modifier
            .height(50.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
    ) {
        Icon(
            Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable {
                    activity?.finish()
                }
        )
    }
}

@Composable
fun AddButton(viewModel: ToDoListViewModel) {
    FloatingActionButton(
        onClick = {
            viewModel.addToDoItem(0)
        },
        // Remove shape parameter for default shape (square with rounded corners)
        shape = RoundedCornerShape(45, 45, 45, 45),
        modifier = Modifier.padding(20.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new to do", tint = Color.Black)
    }
}
