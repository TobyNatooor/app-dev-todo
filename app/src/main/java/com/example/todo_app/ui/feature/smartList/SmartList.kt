package com.example.todo_app.ui.feature.smartList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.rounded.MoreVert
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
import com.example.todo_app.ui.feature.common.DeleteList
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu
import com.example.todo_app.ui.feature.common.DropdownSettingsMenuItem
import com.example.todo_app.ui.feature.common.NameList
import com.example.todo_app.ui.feature.common.ToDoCheckBox
import com.example.todo_app.ui.theme.*

@Composable
fun SmartList(
    toDos: List<ToDo>,
    modifier: Modifier = Modifier,
    viewmodel: SmartListViewModel,
    appBar: @Composable () -> Unit
) {
    val scrollState = rememberLazyListState()
    var showSettings by remember { mutableStateOf(false) }

    SettingsDialog(
        showSettings = showSettings,
        onDismiss = { showSettings = false },
        onConfirm = { showSettings = false }
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
                    Text(
                        text = "Settings",
                        color = primary1,
                        fontSize = 16.sp,
                        fontFamily = dosisFontFamily,
                        modifier = Modifier
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
            /*stickyHeader {
                appBar()
            }*/
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = primary3,
                shape = RoundedCornerShape(4.dp)
            )
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
                color = primary0,
                fontFamily = dosisFontFamily
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
                onDeleteClicked = { viewModel.deleteToDo(toDo) },
                onEditClicked = { viewModel.clickToDoOptions(toDo.id) }
            )
        }
    }
}

@Composable
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
}

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
    showSettings: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    if (showSettings) {
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
                Text("Save", fontFamily = dosisFontFamily)
            }
            },
            dismissButton = {
                Button(
                border = BorderStroke(3.dp, primary3),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary0,
                    contentColor = primary3
                ),
                onClick = onDismiss
            ) {
                Text("Cancel", fontFamily = dosisFontFamily)
            }
            },
            title = {
                Text(text = "Settings")
            },
            text = {
                Text(text = "Settings content goes here.")
            }
        )
    }
}
