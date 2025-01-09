package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu

@Composable
fun ToDoList(
    toDos: List<ToDo>,
    viewmodel: ToDoListViewModel,
    modifier: Modifier = Modifier,
    title: String = ""
) {
    val scrollState = rememberLazyListState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize()
        ) {
            // Title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 60.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 75.dp, bottom = 75.dp)
                )
            }

            // To-do elements
            LazyColumn(
                state = scrollState,
                modifier = Modifier
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

        // Settings dropdown menu
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            DropdownSettingsMenu(
                onRenameClicked = { /* Handle rename on ToDoListScreen */  }
            )
        }
    }
}

@Composable
private fun ToDoItem(viewmodel: ToDoListViewModel, toDo: ToDo, index: Int = 0) {
    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp) // Inner padding for the content inside the box
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ToDoCheckBox(toDo, viewmodel)
            if (toDo.title == null) {
                ToDoTextField(toDo, viewmodel)
            } else {
                Text(
                    text = toDo.title.toString(),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
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
private fun ToDoCheckBox(toDo: ToDo, viewmodel: ToDoListViewModel){
    Checkbox(
        toDo.status.isDone(),
        onCheckedChange = {
            viewmodel.updateToDoItem(
                toDo.copy(
                    status = toDo.status.check()
                )
            )
        }
    )
}
