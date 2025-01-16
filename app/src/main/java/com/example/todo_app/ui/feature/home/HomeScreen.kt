package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.ui.feature.common.*
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.primary2
import com.example.todo_app.ui.theme.primary4
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                coroutineScope.launch {
                    viewModel.searchForTodos("")
                    columnState.animateScrollToItem(0)
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

@Composable
fun AddButton(
    viewModel: HomeViewModel,
    searchQuery: MutableState<String>,
    columnState: LazyListState
) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                searchQuery.value = ""
                viewModel.searchForTodos("")
                columnState.animateScrollToItem(0)
                delay(200L)
                viewModel.addClicked()
            }
        },
        shape = RoundedCornerShape(45, 45, 45, 45),
        containerColor = primary2,
        modifier = Modifier
            .padding(20.dp)
            .border(1.dp, primary4, RoundedCornerShape(45, 45, 45, 45))
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add new list",
            tint = primary4,
        )
    }
}
