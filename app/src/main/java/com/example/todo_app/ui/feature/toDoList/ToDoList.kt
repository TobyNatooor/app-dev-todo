package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.ToDo

@Composable
fun ToDoList(
    toDos: List<ToDo>,
    viewmodel: ToDoListViewModel,
    modifier: Modifier = Modifier,
    title: String = ""
) {
    val scrollState = rememberLazyListState()

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {
        // Title
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 54.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 75.dp, bottom = 75.dp)
        )

        // To-do elements
        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxSize()
        ) {
            if (toDos.isEmpty()) {
                item {
                    Text(
                        text = "No to-do items in this list",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
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
private fun ToDoItem(viewModel: ToDoListViewModel, toDo: ToDo, index: Int = 0) {
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
            ToDoCheckBox(toDo, viewModel)
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
            )
            if (toDo.title == null) {
                ToDoTextField(toDo, viewModel)
            } else {
                Text(
                    text = toDo.title.toString(),
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }

        ToDoOptionsButton(toDo, viewModel,
            modifier = Modifier
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun ToDoTextField(
    toDo: ToDo,
    viewmodel: ToDoListViewModel
) {
    val focusRequester = remember { FocusRequester() }
    var isEnabled by remember { mutableStateOf(true) }
    var title by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val blankTitle = "Unnamed to do item"

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
                viewmodel.updateToDoItem(
                    toDo.copy(title = title)
                )
            }
        }
        BasicTextField(
            value = title,
            onValueChange = { newTitle ->
                title = newTitle
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
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
                        viewmodel.updateToDoItem(
                            toDo.copy(title = title)
                        )
                        isEnabled = false
                    }
                },
            enabled = isEnabled,
        )
        // Hint text when title is blank
        if (title.isBlank()) {
            Text(
                text = "Enter new title",
                color = Color.Gray,
                fontSize = 16.sp,
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
                    color = if (toDo.status.isDone()) MaterialTheme.colorScheme.primary else Color.White,
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
                tint = MaterialTheme.colorScheme.onSecondary,
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
        tint = Color.White,
        modifier = modifier
            .padding(8.dp)
            .clickable {
                viewModel.clickToDoOptions(toDoId = toDo.id)
                focusManager.clearFocus()
            }
    )
}
