package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.ui.feature.common.*
import com.example.todo_app.ui.feature.common.LoadingScreen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    db: AppDatabase,
    navController: NavController
) {
    val columnState = rememberLazyListState()
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db, navController)
    )
    val homeUIState = viewModel.homeState.collectAsState().value
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

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
                viewModel.initializeNewList()
            })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Box(modifier = modifier) {
                HomeContent(homeUIState, viewModel, columnState)
            }
        }
    }
}

@Composable
private fun HomeContent(
    homeUIState: HomeUIState,
    viewModel: HomeViewModel,
    columnState: LazyListState,
    modifier: Modifier = Modifier,
) {
    when (homeUIState) {
        is HomeUIState.Empty -> HomeList(
            lists = ArrayList(),
            viewModel = viewModel,
            columnState = columnState
        )

        is HomeUIState.Data -> HomeList(
            lists = homeUIState.lists,
            viewModel = viewModel,
            columnState = columnState
        )

        else -> LoadingScreen(modifier)
    }
}
