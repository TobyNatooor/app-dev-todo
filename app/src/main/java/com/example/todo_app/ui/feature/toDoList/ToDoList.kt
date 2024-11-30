package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

import com.example.todo_app.model.ToDo

@Composable
fun ToDoList(
    toDos: List<ToDo>,
    viewmodel: ToDoListViewModel,
    modifier: Modifier = Modifier,
    title: String = "",
) {
    val scrollState = rememberLazyListState()
    val toDosWithTitle = listOf(title) + toDos

    LazyColumn(
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
    ) {
        itemsIndexed(toDosWithTitle) { index, item ->
            if (index == 0) {
                Text(
                    item.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 60.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, bottom = 100.dp)
                )
            } else {
                ToDoItem(viewmodel, toDo = item as ToDo, index = index - 1)
            }
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
            ToDoTextField(toDo, viewmodel)
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
    var title by remember { mutableStateOf(toDo.title) }
    var isFocused by remember { mutableStateOf(false) }
    val blankTitle = "Unnamed to do item"

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        LaunchedEffect(Unit) {
            if (title.isBlank()) {
                isEnabled = true
                isFocused = false
                focusRequester.requestFocus()
            }
            else isEnabled = false
        }

        DisposableEffect(Unit) {
            onDispose {
                if (title.isBlank()) {
                    viewmodel.updateToDoItem(
                        toDo.copy(title = blankTitle)
                    )
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
                    if(!isFocused){
                        if(title.isBlank()){
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
                toDo.copy(status = toDo.status.check())
            )
        })
}