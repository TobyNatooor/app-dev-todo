package com.example.todo_app.ui.feature.common

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.State
import com.example.todo_app.ui.feature.home.HomeViewModel
import androidx.compose.ui.unit.Dp
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.toDoList.ToDoListViewModel
import com.example.todo_app.ui.theme.*
import androidx.compose.foundation.combinedClickable
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.MutableState
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.todo_app.model.SortOption
import com.example.todo_app.model.ToDoStatus
import com.example.todo_app.ui.feature.BaseViewModel

@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(65.dp)
            .border(1.dp, neutral4, RoundedCornerShape(25, 25, 25, 25))
    ) {
        FloatingActionButton(
            onClick = onClick,
            // Remove shape parameter for default shape (square with rounded corners)
            // shape = RoundedCornerShape(45, 45, 45, 45),
            containerColor = primary2,
            contentColor = primary1,
            modifier = Modifier
                .padding(0.dp)
                .size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                modifier = Modifier
                    .size(48.dp),
                contentDescription = "Add new item",
                tint = primary4,
            )
        }
    }
}

@Composable
fun NameList(
    title: String?,
    textStyle: TextStyle?,
    modifier: Modifier = Modifier,
    selectAllText: Boolean = false,
    onTitleChange: (String) -> Unit,
    onRenameComplete: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val blankTitle = "Unnamed list"

    var isEnabled by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                title ?: "",
                TextRange((title ?: "").length)
            )
        )
    }

    Box(modifier) {
        LaunchedEffect(Unit) {
            isEnabled = true
            isFocused = false
            focusRequester.requestFocus()
            if (selectAllText) {
                textFieldValue = textFieldValue.copy(
                    selection = TextRange(0, textFieldValue.text.length)
                )
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                if (textFieldValue.text.isBlank()) {
                    textFieldValue = TextFieldValue(blankTitle)
                }
                onTitleChange(textFieldValue.text)
                onRenameComplete()
            }
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue -> textFieldValue = newValue },
            textStyle = textStyle ?: TextStyle(
                fontSize = 20.sp,
                color = neutral0,
                fontFamily = dosisFontFamily
            ),
            cursorBrush = SolidColor(neutral0),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .focusRequester(focusRequester)
                // Handle onTitleChange callback when unfocused
                .onFocusChanged {
                    isFocused = !isFocused
                    if (!isFocused) {
                        if (textFieldValue.text.isBlank()) {
                            textFieldValue = TextFieldValue(blankTitle)
                        }
                        onTitleChange(textFieldValue.text)
                        isEnabled = false
                        onRenameComplete()
                    }
                }
                .onKeyEvent {
                    onTitleChange(textFieldValue.text)
                    true
                },
            enabled = isEnabled
        )

        if (textFieldValue.text.isBlank()) {
            Text(
                text = "Enter new title",
                style = (textStyle ?: TextStyle(
                    fontSize = 20.sp,
                    color = neutral0,
                    fontFamily = dosisFontFamily
                )).copy(color = neutral1)
            )
        }
    }
}

@Composable
fun DeleteList(
    listId: Int,
    title: String,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = primary0,
        titleContentColor = primary4,
        textContentColor = primary3,
        onDismissRequest = onDismiss,
        title = { Text("Delete list \"$title\"?", fontFamily = dosisFontFamily) },
        text = { Text("Are you sure you want to delete this list?", fontFamily = dosisFontFamily) },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary3,
                    contentColor = primary0
                ),
                onClick = {
                    onDelete(listId)
                    onDismiss()
                }
            ) {
                Text("Delete", fontFamily = dosisFontFamily)
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
        }
    )
}

@Composable
fun FavoriteButton(
    isFavorite: State<Boolean>,
    onFavClicked: () -> Unit,
){

    val icon = if (isFavorite.value) Icons.Rounded.Star
    else Icons.Rounded.StarBorder

    val color = if (isFavorite.value) yellow2
    else neutral0

    IconButton(onClick = { onFavClicked() }) {
        Icon(icon, contentDescription = "Favorite", tint = color)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoCheckBox(
    toDo: ToDo,
    viewModel: BaseViewModel,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    val color = when (toDo.status) {
        ToDoStatus.DONE -> green1
        ToDoStatus.IN_PROGRESS -> yellow1
        ToDoStatus.CANCELED -> red1
        else -> neutral1
    }
    val iconColor = when (toDo.status) {
        ToDoStatus.DONE -> green4
        ToDoStatus.IN_PROGRESS -> yellow4
        ToDoStatus.CANCELED -> red4
        else -> neutral1
    }
    val imageVector = when (toDo.status) {
        ToDoStatus.DONE -> Icons.Filled.Check
        ToDoStatus.IN_PROGRESS -> Icons.Filled.Update
        ToDoStatus.CANCELED -> Icons.Filled.Close
        else -> Icons.Filled.Check
    }
    Log.d("ABCDEF", "${toDo.title} ${imageVector}")

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        ChooseTodoStatus(viewModel, toDo, showDialog, size)
    }

    Box {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .padding(8.dp)
                .size(size)
                .background(
                    color = color,
                    shape = RoundedCornerShape(5.dp)
                )
                .combinedClickable(
                    onClick = {
                        viewModel.updateToDoItem(
                            toDo.copy(status = toDo.status.check())
                        )
                    },
                    onLongClick = {
                        showDialog.value = true
                    },
                ),
        )
        Icon(
            imageVector = imageVector,
            contentDescription = "Check Icon",
            tint = iconColor,
            modifier = Modifier
                .size(size * 1.1f)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ChooseTodoStatus(
    viewModel: BaseViewModel,
    toDo: ToDo,
    showDialog: MutableState<Boolean>,
    size: Dp
) {
    AlertDialog(
        containerColor = primary2,
        onDismissRequest = { showDialog.value = false },
        title = {},
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
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
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.NOT_DONE)
                                )
                                showDialog.value = false
                            }
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = green2,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.DONE)
                                )
                                showDialog.value = false
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Check Icon",
                            tint = green4,
                            modifier = Modifier
                                .size(size * 1.1f)
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
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.IN_PROGRESS)
                                )
                                showDialog.value = false
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Update,
                            contentDescription = "Check Icon",
                            tint = yellow4,
                            modifier = Modifier
                                .size(size * 1.1f)
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
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.CANCELED)
                                )
                                showDialog.value = false
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Check Icon",
                            tint = red4,
                            modifier = Modifier
                                .size(size * 1.1f)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun SortButton(
    onSortClicked: ((SortOption) -> Unit)? = null
) {
    val sortOptions = listOf(SortOption.NAME, SortOption.CREATED, SortOption.RECENT)

    var expanded by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = neutral4
        ),
        onClick = { expanded = true }
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Sort,
            contentDescription = "Sort",
            tint = neutral1,
            modifier = Modifier.size(24.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(neutral1)
        ) {
            Text(
                text = "Sort by",
                color = neutral4,
                fontSize = 16.sp,
                fontFamily = dosisFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            HorizontalDivider(
                color = neutral4,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            sortOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSortClicked?.invoke(option)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = option.toString(),
                            color = neutral4,
                            fontSize = 16.sp,
                            fontFamily = dosisFontFamily,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                )
            }
        }
    }
}