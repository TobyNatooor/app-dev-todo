package com.example.todo_app.ui.feature.home

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.ui.feature.common.EmptyScreen
import com.example.todo_app.ui.feature.common.LoadingScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    db: AppDatabase
) {
    val viewmodel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db, navController)
    )
    val homeUIState = viewmodel.homeState.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { AddButton(viewmodel) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(modifier = modifier) {
                HomeContent(homeUIState, viewmodel)
            }
        }

    }
}

@Composable
fun AddButton(viewModel: HomeViewModel) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            coroutineScope.launch { viewModel.addList() }
        },
        // Remove shape parameter for default shape (square with rounded corners)
        shape = RoundedCornerShape(45, 45, 45, 45),
        modifier = Modifier.padding(20.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new list", tint = Color.Black)
    }
}

@Composable
private fun HomeContent(
    homeUIState: HomeUIState,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    when (homeUIState) {
        is HomeUIState.Empty -> EmptyScreen(
            modifier = modifier,
            title = "Home",
            text = "No checklists yet"
        )
        is HomeUIState.Loading -> LoadingScreen(modifier)
        is HomeUIState.Data -> HomeList(
            lists = homeUIState.lists,
            viewModel = viewModel
        )
    }
}

