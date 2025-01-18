package com.example.todo_app.ui.feature.smartList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme
import androidx.compose.foundation.background
import com.example.todo_app.ui.theme.*

@Composable
fun SmartListScreen(
    modifier: Modifier = Modifier,
    db: AppDatabase,
    navController: NavController,
    appBar: @Composable () -> Unit,
) {
    val viewModel: SmartListViewModel = viewModel(
        factory = SmartListViewModelFactory(db, navController)
    )
    val toDosUIState = viewModel.toDosState.collectAsState().value

    TodoappTheme {
        Scaffold( 
            modifier = Modifier
                .fillMaxSize()
                .background(primary4)
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                Box(modifier = modifier) {
                    when (toDosUIState) {
                        is ToDosUIState.Loading -> LoadingScreen(modifier = modifier)
                        is ToDosUIState.Data -> SmartList(
                            toDos = toDosUIState.toDos,
                            viewmodel = viewModel,
                            appBar = appBar
                        )
                    }
                }
            }
        }
    }
}
