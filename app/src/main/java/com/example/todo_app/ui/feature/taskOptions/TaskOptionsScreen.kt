package com.example.todo_app.ui.feature.taskOptions

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
fun TaskOptionsScreen(
    modifier: Modifier = Modifier,
    taskId: Int,
    appBar: @Composable () -> Unit,
    db: AppDatabase
) {
    val viewmodel: TaskOptionsViewModel = viewModel(
        key = "TaskOptionsViewModel_$taskId",
        factory = TaskOptionsViewModelFactory(taskId, db)
    )
    val taskUIState = viewmodel.taskState.collectAsState().value
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
                    TaskContent(taskUIState, viewmodel, db)
                }
            }
        }
    }
}

@Composable
private fun TaskContent(
    taskUIState: TaskUIState,
    viewmodel: TaskOptionsViewModel,
    db: AppDatabase,
    modifier: Modifier = Modifier
) {
    when (taskUIState) {
        is TaskUIState.Loading -> LoadingScreen(
            modifier = modifier
        )

        is TaskUIState.Data -> TaskOptions(
            task = taskUIState.task,
            viewmodel = viewmodel,
            db = db,
            modifier = modifier
        )
    }
}
