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
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme
import androidx.compose.foundation.background
import com.example.todo_app.ui.theme.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun SmartListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    appBar: @Composable () -> Unit,
) {
    val viewModel: SmartListViewModel = viewModel(
        factory = SmartListViewModel.createFactory(navController)
    )
    val toDosUIState = viewModel.toDosState.collectAsState().value
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
                }
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
