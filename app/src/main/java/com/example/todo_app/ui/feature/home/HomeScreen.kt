package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.ui.feature.common.*
import com.example.todo_app.ui.feature.common.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    db: AppDatabase,
    navController: NavController
) {
    val gridState = rememberLazyGridState()
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db, navController)
    )
    val homeUIState = viewModel.homeState.collectAsState().value
    val searchQuery = remember { mutableStateOf("") }
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
                coroutineScope.launch {
                    searchQuery.value = ""
                    viewModel.searchForTodos("")
                    gridState.animateScrollToItem(0)
                    delay(200L)
                    viewModel.addClicked()
                }
            } )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Box(modifier = modifier) {
                HomeContent(homeUIState, searchQuery, focusManager, viewModel, gridState)
            }
        }
    }
}

@Composable
private fun HomeContent(
    homeUIState: HomeUIState,
    searchQuery: MutableState<String>,
    focusManager: FocusManager,
    viewModel: HomeViewModel,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
) {
    when (homeUIState) {
        is HomeUIState.Empty -> HomeList(
            lists = ArrayList(),
            viewModel = viewModel,
            searchQuery = searchQuery,
            focusManager = focusManager,
            gridState = gridState
        )

        is HomeUIState.Data -> HomeList(
            lists = homeUIState.lists,
            viewModel = viewModel,
            searchQuery = searchQuery,
            focusManager = focusManager,
            gridState = gridState
        )

        else -> LoadingScreen(modifier)
    }
}
