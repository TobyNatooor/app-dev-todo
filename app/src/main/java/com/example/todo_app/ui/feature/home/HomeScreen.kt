package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.clickable
import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.ui.feature.common.LoadingScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    db: AppDatabase
) {
    val gridState = rememberLazyGridState()
    val viewmodel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db, navController)
    )
    val homeUIState = viewmodel.homeState.collectAsState().value

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            },
        floatingActionButton = { AddButton(viewmodel,gridState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Box(modifier = modifier) {
                HomeContent(homeUIState, viewmodel, gridState = gridState)
            }
        }

    }
}

@Composable
fun AddButton(viewModel: HomeViewModel, gridState: LazyGridState) {
    val coroutineScope = rememberCoroutineScope()

    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                viewModel.addList()
                val lastIndex = gridState.layoutInfo.totalItemsCount - 1
                if (lastIndex >= 0) {
                    delay(100L)
                    gridState.animateScrollToItem(lastIndex)
                }
            }
        },
        // Remove shape parameter for default shape (square with rounded corners)
        shape = RoundedCornerShape(45, 45, 45, 45),
        contentColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.padding(20.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add new list",
            tint = Color(0xFF1E1E1E)
        )
    }
}

@Composable
private fun HomeContent(
    homeUIState: HomeUIState,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    gridState: LazyGridState
) {
    when (homeUIState) {
/*        is HomeUIState.Empty -> EmptyScreen(
            modifier = modifier,
            title = "Home",
            text = "No checklists yet"
        )*/
        is HomeUIState.Empty -> HomeList(
            lists = ArrayList(),
            viewModel = viewModel,
            gridState = gridState
        )
        is HomeUIState.Data -> HomeList(
            lists = homeUIState.lists,
            viewModel = viewModel,
            gridState = gridState
        )
        else -> LoadingScreen(modifier)
    }
}
