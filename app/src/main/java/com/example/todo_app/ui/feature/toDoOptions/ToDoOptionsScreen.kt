package com.example.todo_app.ui.feature.toDoOptions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.repository.ChecklistRepository
import com.example.todo_app.repository.ToDoRepository
import com.example.todo_app.ui.feature.common.LoadingScreen
import com.example.todo_app.ui.theme.TodoappTheme
import com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun ToDoOptionsScreen(
    modifier: Modifier = Modifier,
    toDoId: Int,
    appBar: @Composable () -> Unit,
    getLocation: ((Place?) -> Unit?) -> Unit,
    checklistRepository: ChecklistRepository,
    toDoRepository: ToDoRepository,
    navController: NavHostController
) {
    val viewModel: ToDoOptionsViewModel = viewModel(
        key = "ToDoOptionsViewModel_$toDoId",
        factory = ToDoOptionsViewModelFactory(toDoId, toDoRepository, checklistRepository)
    )
    val toDoUIState by viewModel.toDoState.collectAsState()
    val focusManager = LocalFocusManager.current
    val cameraPositionState = rememberCameraPositionState {
        position = fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }

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
        ) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                //appBar()
                Box(modifier = modifier) {
                    ToDoContent(
                        toDoUIState,
                        viewModel,
                        appBar,
                        cameraPositionState,
                        getLocation,
                        navController
                    )
                }
            }
        }
    }
}

@Composable
private fun ToDoContent(
    toDoUIState: ToDoUIState,
    viewModel: ToDoOptionsViewModel,
    appBar: @Composable () -> Unit,
    cameraPositionState: CameraPositionState,
    getLocation: ((Place?) -> Unit?) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    when (toDoUIState) {
        is ToDoUIState.Loading -> LoadingScreen(
            modifier = modifier
        )

        is ToDoUIState.Data -> ToDoOptions(
            toDo = toDoUIState.toDo,
            checklists = toDoUIState.checklists,
            viewmodel = viewModel,
            cameraPositionState = cameraPositionState,
            getLocation = getLocation,
            navController = navController,
            modifier = modifier,
            appBar = appBar
        )
    }
}
