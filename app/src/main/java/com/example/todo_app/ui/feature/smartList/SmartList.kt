package com.example.todo_app.ui.feature.smartList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu
import com.example.todo_app.ui.feature.common.DropdownSettingsMenuItem
import com.example.todo_app.ui.feature.common.DeleteDialog
import com.example.todo_app.ui.feature.common.NameList
import com.example.todo_app.ui.feature.common.ToDoCheckBox
import com.example.todo_app.model.SmartSettings
import com.example.todo_app.ui.feature.common.CustomDropdownMenu
import com.example.todo_app.ui.theme.*
import androidx.compose.foundation.border
import androidx.compose.runtime.State
import com.google.maps.android.compose.GoogleMap
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import androidx.compose.foundation.layout.wrapContentHeight
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SmartList(
    toDos: List<ToDo>,
    modifier: Modifier = Modifier,
    viewmodel: SmartListViewModel,
    appBar: @Composable () -> Unit
) {
    val scrollState = rememberLazyListState()
    var showSettings by remember { mutableStateOf(false) }
    val settings = viewmodel.smartSettings.collectAsState()

    SettingsDialog(
        settings = settings,
        viewModel = viewmodel,
        showSettings = showSettings,
        onDismiss = { showSettings = false },
        onConfirm = { showSettings = false },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxSize()
        ) {
            item {
                // Settings dropdown menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        Icons.Filled.Tune,
                        contentDescription = "Settings",
                        tint = primary2,
                        modifier = Modifier
                            .size(32.dp)
                            .aspectRatio(1f)
                            .clickable {
                                showSettings = true
                            }
                    )
                }
            }
            item {
                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, bottom = 75.dp)
                ) {
                    Text(
                        color = primary1,
                        text = "Smart List",
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontSize = 54.sp, fontFamily = dosisFontFamily),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            stickyHeader {
                appBar()
            }
            // To-do elements

            if (toDos.isEmpty()) {
                item {
                    Text(
                        color = primary1,
                        text = "No to-do items in your smart list, try changing the settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = dosisFontFamily
                    )
                }
            } else {
                itemsIndexed(toDos) { index, item ->
                    ToDoItem(viewmodel, toDo = item, index = index)
                }
            }
        }
    }
}

@Composable
private fun ToDoItem(viewModel: SmartListViewModel, toDo: ToDo, index: Int = 0) {
    val cameraPositionState = rememberCameraPositionState {
        position = fromLatLngZoom(LatLng(0.0, 0.0), 10f)
    }
    var markerPosition = remember { LatLng(0.0, 0.0) }
    val markerState = remember { MarkerState(position = markerPosition) }
    var isExapnded by remember { mutableStateOf(false) }
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
                color = primary3,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Column () {    
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
                    color = primary0,
                    fontFamily = dosisFontFamily,
                    modifier = Modifier
                        .clickable {
                            isExapnded = !isExapnded
                        }
                )
                Spacer(
                    modifier = Modifier.weight(1f)
                )
                DropdownSettingsMenu(
                    actions = listOf(
                        DropdownSettingsMenuItem.Rename,
                        DropdownSettingsMenuItem.Delete,
                        DropdownSettingsMenuItem.Edit
                    ),
                    onRenameClicked = { /* TODO */},
                    onDeleteClicked = { showDeleteDialog = true },
                    onEditClicked = { viewModel.clickToDoOptions(toDo.id) 
                    }
                )
            }
            if (isExapnded) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ){
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
                                    color = primary0,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Text(
                                "Specify location to view map",
                                textAlign = TextAlign.Center,
                                fontFamily = dosisFontFamily,
                                color = primary3,
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
                            color = primary0,
                            fontSize = 16.sp,
                            fontFamily = dosisFontFamily,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                        )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = primary0,
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = formatDeadline(toDo.deadline),
                                color = primary3,
                                fontSize = 16.sp,
                                fontFamily = dosisFontFamily
                            )
                        }
                    }
                    //description
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Description",
                            color = primary0,
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
                                    color = primary0,
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
                                color = primary3,
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

/*@Composable
fun ToDoCheckBox(
    toDo: ToDo,
    viewModel: SmartListViewModel,
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
    viewModel: SmartListViewModel,
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
fun SettingsDialog(
    settings: State<SmartSettings>,
    viewModel: SmartListViewModel,
    showSettings: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showSettings) {
        var deadlineWithin by remember { mutableStateOf(settings.value.deadlineWithinDays.toString()) }
        val checkLists by viewModel.getCheckLists().collectAsState()
        val dropdownSelections = checkLists.map { it -> DropdownOptionItem(it.id, it.title.toString()) }.toMutableList()
        dropdownSelections.add(DropdownOptionItem(-1, "All lists"))
        val notDoneOutlineColor = if (settings.value.includeNotDone) primary4 else primary0
        val doneOutlineColor = if (settings.value.includeDone) primary4 else primary0
        val inProgressOutlineColor = if (settings.value.includeInProgress) primary4 else primary0
        val cancelledOutlineColor = if (settings.value.includeCancelled) primary4 else primary0
        AlertDialog(
            containerColor = primary0,
            titleContentColor = primary4,
            textContentColor = primary3,
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary3,
                    contentColor = primary0
                ),
                onClick = {
                    onConfirm()
                }
                ) {
                    Text("Done", fontFamily = dosisFontFamily)
                }
            },
            title = {
                Text(text = "Include ToDos")
            },
            text = {
                Column (modifier = Modifier.padding(5.dp, 5.dp)) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = primary4,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Status",
                        fontSize = 18.sp,
                        color = primary4,
                        fontFamily = dosisFontFamily
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = neutral1,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .border(
                                    BorderStroke(2.dp, notDoneOutlineColor),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clickable {
                                    viewModel.setSettings(settings.value.copy(includeNotDone = !settings.value.includeNotDone))
                                }
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = green2,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .border(
                                    BorderStroke(2.dp, doneOutlineColor),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clickable {
                                    viewModel.setSettings(settings.value.copy(includeDone = !settings.value.includeDone))
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Check Icon",
                                tint = green4,
                                modifier = Modifier
                                    .size(26.dp * 1.1f)
                                    .align(Alignment.Center)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = yellow2,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .border(
                                    BorderStroke(2.dp, inProgressOutlineColor),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clickable {
                                    viewModel.setSettings(settings.value.copy(includeInProgress = !settings.value.includeInProgress))
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Update,
                                contentDescription = "Check Icon",
                                tint = yellow4,
                                modifier = Modifier
                                    .size(26.dp * 1.1f)
                                    .align(Alignment.Center)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = red2,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .border(
                                    BorderStroke(2.dp, cancelledOutlineColor),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .clickable {
                                    viewModel.setSettings(settings.value.copy(includeCancelled = !settings.value.includeCancelled))
                                }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Check Icon",
                                tint = red4,
                                modifier = Modifier
                                    .size(26.dp * 1.1f)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = primary4,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp).padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier
                    ) {
                        Text(
                            text = "Deadlines less than ",
                            fontSize = 18.sp,
                            color = primary4,
                            fontFamily = dosisFontFamily,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        BasicTextField(
                            value = deadlineWithin,
                            onValueChange = { newValue ->
                                if (newValue.all { it.isDigit() } && newValue.length <= 2) {
                                    deadlineWithin = newValue
                                    viewModel.setSettings(settings.value.copy(deadlineWithinDays = newValue.toIntOrNull() ?: 0))
                                }
                            },
                            textStyle = TextStyle(
                                color = primary4,
                                fontSize = 18.sp,
                                fontFamily = dosisFontFamily
                            ),
                            modifier = Modifier
                                .background(color = primary1, shape = RoundedCornerShape(5.dp))
                                .padding(8.dp)
                                .width(20.dp)
                        )
                        Text(
                            text = " days away",
                            fontSize = 18.sp,
                            color = primary4,
                            fontFamily = dosisFontFamily,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = primary4,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp).padding(bottom = 8.dp)
                    )
                    DropdownMenuOption(
                        settings = settings,
                        viewModel = viewModel,
                        height = 42.dp,
                        contentAlign = Alignment.Center,
                        options = dropdownSelections,
                    )
                }
            }
        )
    }
}

@Composable
private fun DropdownMenuOption(
    viewModel: SmartListViewModel,
    settings: State<SmartSettings>,
    height: Dp,
    options: List<DropdownOptionItem>,
    contentAlign: Alignment = Alignment.Center
) {
    var selectedId = settings.value.listId
    var expanded by remember { mutableStateOf(false) }
    if (options.none { it.id == settings.value.listId }) {
        viewModel.setSettings(settings.value.copy(listId = -1))
        selectedId = -1
    }
    var selectedOption: DropdownOptionItem? by remember { mutableStateOf(
        options.find { it.id == selectedId }
    ) }
    val shape = RoundedCornerShape(12.dp)
    val textAlign = when (contentAlign) {
        Alignment.Center -> TextAlign.Center
        Alignment.TopStart -> TextAlign.Left
        else -> TextAlign.Center
    }
    val modifier = Modifier.fillMaxWidth()

    Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
        Box(
            modifier = modifier
                .height(height)
                .background(color = primary0, shape = shape)
                .clickable { expanded = !expanded },
            contentAlignment = contentAlign
        ) {
            Text(
                text = "From: "+(selectedOption?.title ?: "No lists"),
                fontSize = 18.sp,
                fontFamily = dosisFontFamily,
                color = primary4,
                textAlign = textAlign,
                modifier = Modifier.padding(10.dp)
            )
        }

        CustomDropdownMenu(
            height = 150.dp,
            modifier = modifier
                .background(color = primary1, shape = shape)
                .padding(vertical = 4.dp),
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = primary4,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            contentAlign = contentAlign,
            expanded = expanded,
            items = options,
            onDismiss = { expanded = false },
            onItemSelected = {
                selectedOption = it
                viewModel.setSettings(settings.value.copy(listId = it.id))
            },
            itemContent = { item, index ->
                Box(
                    modifier = modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    contentAlignment = contentAlign
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontFamily = dosisFontFamily,
                        color = primary4,
                        textAlign = textAlign
                    )
                }
            }
        )
    }
}

data class DropdownOptionItem(
    // TODO: Hej Max - 'val id' er ændret fra 'Int?' til 'Int', og standard værdi er '-1'
    val id: Int,
    val title: String
)