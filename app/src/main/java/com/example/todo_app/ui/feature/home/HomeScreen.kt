package com.example.todo_app.ui.feature.home

import com.example.todo_app.data.AppDatabase
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.todo_app.model.CheckList
import com.example.todo_app.ui.feature.common.EmptyScreen
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.feature.toDoList.AddButton
import com.example.todo_app.ui.feature.toDoList.ToDoList
import com.example.todo_app.ui.feature.toDoList.ToDoListViewModel
import com.example.todo_app.ui.feature.toDoList.ToDoListViewModelFactory
import com.example.todo_app.ui.feature.toDoList.ToDosContent
import com.example.todo_app.ui.feature.toDoList.ToDosUIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    db: AppDatabase
) {
    val viewmodel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(db)
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

    Scaffold(
        floatingActionButton = {
            ListButton(lists, db)
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(horizontal = 40.dp),
            modifier = modifier.padding(paddingValues)
        ) {
            item(span = { GridItemSpan(2) }) {
                Text(
                    "My Lists",
                    textAlign = TextAlign.Center,
                    fontSize = 60.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 100.dp, bottom = 100.dp)
                )
            }
            items(lists.size) { index ->
                ListCard(lists[index], navController)
            }
        }
    }

    /*
    =================================================
     */
    val lists = remember { mutableStateListOf<CheckList>() }
    // lists.clear()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val fetchedList = db.checkListDao().getAll()
            withContext(Dispatchers.Main){
                lists.addAll(fetchedList)
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
    viewmodel: HomeViewModel,
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
            modifier = modifier,
            title = "title",
            toDos = toDosUIState.toDos,
            viewmodel = viewmodel
        )
    }
}

