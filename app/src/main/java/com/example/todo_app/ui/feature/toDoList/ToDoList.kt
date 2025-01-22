package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import com.google.maps.android.compose.GoogleMap
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.DeleteDialog
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu
import com.example.todo_app.ui.feature.common.FavoriteButton
import com.example.todo_app.ui.feature.common.DropdownSettingsMenuItem
import com.example.todo_app.ui.feature.common.NameList
import com.example.todo_app.ui.feature.common.ToDoCheckBox
import com.example.todo_app.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun ToDoList(
    toDos: List<ToDo>,
    listId: Int,
    viewmodel: ToDoListViewModel,
    modifier: Modifier = Modifier,
    title: String = "",
    appBar: @Composable () -> Unit
) {
    val scrollState = rememberLazyListState()
    var listTitle by remember { mutableStateOf(title) }
    var isNaming by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val addingToDo = viewmodel.addingNewToDo.collectAsState()
    val isFavorite = viewmodel.isFavorite.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                // Settings dropdown menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FavoriteButton(isFavorite) { viewmodel.favoriteClicked() }
                    DropdownSettingsMenu(
                        isFavorite = isFavorite.value,
                        actions = listOf(
                            DropdownSettingsMenuItem.Rename,
                            DropdownSettingsMenuItem.Delete,
                            DropdownSettingsMenuItem.Favorite
                        ),
                        onRenameClicked = { isNaming = true },
                        onDeleteClicked = { showDeleteDialog = true },
                        onFavoriteClicked = { viewmodel.favoriteClicked() },
                    )
                }
            }

            item {
                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 68.dp, bottom = 100.dp)
                ) {
                    if (isNaming) {
                        NameList(
                            title = listTitle,
                            textStyle = TextStyle(
                                fontSize = 54.sp,
                                color = neutral1,
                                textAlign = TextAlign.Center,
                                fontFamily = dosisFontFamily
                            ),
                            modifier = Modifier
                                .fillMaxWidth(),
                            onTitleChange = { newTitle ->
                                viewmodel.updateList(listId, newTitle)
                                listTitle = newTitle
                            },
                            onRenameComplete = {
                                isNaming = false
                            }
                        )
                    } else {
                        Text(
                            color = neutral1,
                            text = listTitle,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontSize = 54.sp, fontFamily = dosisFontFamily),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }

            stickyHeader {
                appBar()
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    if (addingToDo.value) {
                        NewToDoItem(viewmodel, listId)
                    }

                    // To-do elements
                    if (toDos.isEmpty() && !addingToDo.value) {
                        Text(
                            color = neutral1,
                            text = "No to-do items in this list",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosisFontFamily
                        )
                    } else {
                        toDos.forEachIndexed { index, toDo ->
                            ToDoItem(viewmodel, toDo = toDo, index = index)
                        }
                    }
                }
            }
        }

        if (showDeleteDialog) {
            DeleteDialog(
                id = listId,
                title = "Delete list \"$title\"?",
                text = "Are you sure you want to delete this list?",
                onDelete = { viewmodel.deleteList(listId) },
                onDismiss = { showDeleteDialog = false },
            )
        }
    }
}

@Composable
private fun ToDoItem(viewModel: ToDoListViewModel, toDo: ToDo, index: Int = 0) {
    val cameraPositionState = rememberCameraPositionState {
        position = fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }
    var markerPosition = remember { LatLng(0.0, 0.0) }
    val markerState = remember { MarkerState(position = markerPosition) }
    var isExpanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    if (showDeleteDialog) {
        DeleteDialog(
            id = toDo.id,
            title = "Delete todo \"${toDo.title}\"?",
            text = "Are you sure you want to delete this todo?",
            onDelete = { viewModel.deleteToDo(toDo) },
            onDismiss = { showDeleteDialog = false },
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = neutral2,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    isExpanded = !isExpanded
                }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ToDoCheckBox(toDo, viewModel, 26.dp)
                Spacer(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
                Text(
                    text = toDo.title,
                    fontSize = 18.sp,
                    color = neutral0,
                    fontFamily = dosisFontFamily
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                DropdownSettingsMenu(
                    actions = listOf(
                        DropdownSettingsMenuItem.Delete,
                        DropdownSettingsMenuItem.Edit
                    ),
                    onDeleteClicked = { showDeleteDialog = true },
                    onEditClicked = { viewModel.clickToDoOptions(toDo.id) },
                    modifier = Modifier
                        .offset(x = 8.dp)
                )
            }

            if (isExpanded) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    //map
                    if (toDo.location != null && toDo.latitude != null && toDo.longitude != null) {
                        GoogleMap(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            cameraPositionState = cameraPositionState
                        ) {
                            // Add a marker
                            markerPosition = LatLng(toDo.latitude, toDo.longitude)
                            markerState.position = markerPosition
                            cameraPositionState.position = fromLatLngZoom(markerPosition, 10f)
                            Marker(
                                title = toDo.location,
                                state = markerState
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(
                                    color = neutral1,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                "Specify location to view map",
                                textAlign = TextAlign.Center,
                                fontFamily = dosisFontFamily,
                                color = neutral4,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                                    .wrapContentHeight()
                            )
                        }
                    }

                    //deadline
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Deadline",
                            color = neutral0,
                            fontSize = 16.sp,
                            fontFamily = dosisFontFamily,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = neutral1,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = formatDeadline(toDo.deadline),
                                color = neutral3,
                                fontSize = 16.sp,
                                fontFamily = dosisFontFamily
                            )
                        }
                    }

                    //description
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Description",
                            color = neutral0,
                            fontSize = 16.sp,
                            fontFamily = dosisFontFamily,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(50.dp)
                                .background(
                                    color = neutral1,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = if (toDo.description.length > 30) {
                                    toDo.description.take(27) + "..."
                                } else {
                                    toDo.description
                                },
                                color = neutral3,
                                fontSize = 16.sp,
                                fontFamily = dosisFontFamily
                            )
                        }
                    }
                }
            }
        }
    }
}

fun formatDeadline(deadline: LocalDateTime?): String {
    if(deadline == null) return "00/00/00"
    return deadline.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
}

@Composable
private fun ToDoTextField(
    viewmodel: ToDoListViewModel
) {
    val blankTitle = "Unnamed to do item"
    val focusRequester = remember { FocusRequester() }
    var isEnabled by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LaunchedEffect(Unit) {
            isEnabled = true
            isFocused = false
            focusRequester.requestFocus()
        }
        DisposableEffect(Unit) {
            onDispose {
                if (title.isBlank()) {
                    title = blankTitle
                }
            }
        }
        BasicTextField(
            value = title,
            onValueChange = { newTitle ->
                title = newTitle
            },
            singleLine = true,
            textStyle = TextStyle(
                color = neutral0,
                fontSize = 18.sp,
                fontFamily = dosisFontFamily
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .focusRequester(focusRequester)
                // Handle title update to Room SQL when unfocused
                .onFocusChanged {
                    isFocused = !isFocused
                    if (!isFocused) {
                        if (title.isBlank()) {
                            title = blankTitle
                        }
                        viewmodel.addToDoItem(title)
                        isEnabled = false
                    }
                },
            enabled = isEnabled,
        )
        // Hint text when title is blank
        if (title.isBlank()) {
            Text(
                text = "Enter new title",
                color = neutral1,
                fontSize = 16.sp,
                fontFamily = dosisFontFamily,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            )
        }
    }
}

/*@Composable
fun ToDoCheckBox(
    toDo: ToDo,
    viewModel: ToDoListViewModel,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Box {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .padding(8.dp)
                .size(size)
                .background(
                    color = if (toDo.status.isDone()) green1 else neutral1,
                    shape = RoundedCornerShape(5.dp)
                )
                .clickable {
                    viewModel.updateToDoItem(
                        toDo.copy(status = toDo.status.check())
                    )
                }
        )
        if (toDo.status.isDone()) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Check Icon",
                tint = green4,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(size * 1.1f)
            )
        }
    }
}*/

@Composable
fun ToDoOptionsButton(
    toDo: ToDo,
    viewModel: ToDoListViewModel,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Icon(
        imageVector = Icons.Rounded.MoreVert,
        contentDescription = "ToDo Options Icon",
        tint = neutral0,
        modifier = modifier
            .padding(8.dp)
            .clickable {
                viewModel.clickToDoOptions(toDoId = toDo.id)
                focusManager.clearFocus()
            }
    )
}

@Composable
private fun NewToDoItem(viewModel: ToDoListViewModel, listId: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .size(26.dp)
                        .background(
                            color = neutral1,
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
            ToDoTextField(viewModel)
        }
    }
}