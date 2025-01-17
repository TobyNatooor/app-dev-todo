package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.todo_app.ui.feature.common.NameList
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
                    DropdownSettingsMenu(
                        isFavorite = isFavorite.value,
                        onRenameClicked = { isNaming = true },
                        onFavoriteClicked = { viewmodel.favoriteClicked() },
                        onDeleteClicked = { showDeleteDialog = true }
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
            if (addingToDo.value){
                    item { NewToDoItem(viewmodel, listId) }
            }
            // To-do elements

            if (toDos.isEmpty()) {
                item {
                    Text(
                        color = neutral1,
                        text = "No to-do items in this list",
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

        if (showDeleteDialog) {
            DeleteList(
                listId = listId,
                title = listTitle,
                onDelete = { id -> viewmodel.deleteList(listId) },
                onDismiss = { showDeleteDialog = false }
            )
        }
    }
}

@Composable
private fun ToDoItem(viewModel: ToDoListViewModel, toDo: ToDo, index: Int = 0) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = neutral2,
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
                text = toDo.title.toString(),
                fontSize = 18.sp,
                color = neutral0,
                fontFamily = dosisFontFamily
            )
        }
        ToDoOptionsButton(
            toDo, viewModel,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )
    }
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

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
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
                fontSize = 16.sp,
                fontFamily = dosisFontFamily
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

@Composable
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
}

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
                        .padding(8.dp)
                        .size(28.dp)
                        .background(
                            color = Color.White,
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