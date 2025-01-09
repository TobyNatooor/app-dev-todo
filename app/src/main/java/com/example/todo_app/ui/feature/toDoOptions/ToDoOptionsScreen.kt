package com.example.todo_app.ui.feature.toDoOptions

import androidx.compose.foundation.clickable
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
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme

@Composable
fun ToDoOptionsScreen(
    modifier: Modifier = Modifier,
    toDoId: Int,
    appBar: @Composable () -> Unit,
    db: AppDatabase
) {
    val viewmodel: ToDoOptionsViewModel = viewModel(
        key = "ToDoOptionsViewModel_$toDoId",
        factory = ToDoOptionsViewModelFactory(toDoId, db)
    )
    val toDoUIState = viewmodel.toDoState.collectAsState().value
    val focusManager = LocalFocusManager.current

    TodoappTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                appBar()
                Box(modifier = modifier) {
                    ToDoContent(toDoUIState, viewmodel, db)
                }
            }
        }
    }
}

@Composable
private fun ToDoContent(
    toDoUIState: ToDoUIState,
    viewmodel: ToDoOptionsViewModel,
    db: AppDatabase,
    modifier: Modifier = Modifier
) {
    when (toDoUIState) {
        is ToDoUIState.Loading -> LoadingScreen(
            modifier = modifier
        )

        is ToDoUIState.Data -> ToDoOptions(
            toDo = toDoUIState.toDo,
            viewmodel = viewmodel,
            db = db,
            modifier = modifier
        )
    }
}
