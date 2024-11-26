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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

import com.example.todo_app.model.ToDo
import kotlinx.coroutines.launch


@Composable
fun ToDoList(
    toDos: List<ToDo>,
    viewmodel: ToDoListViewModel,
    modifier: Modifier = Modifier,
    title: String = ""
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
    val coroutineScope = rememberCoroutineScope()
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
            Checkbox(
                //TODO: "status" has 3 different states. Checkbox only checks binary states
                toDo.status.isDone(),
                onCheckedChange = {
                    coroutineScope.launch {
                        viewmodel.updateToDoItem(toDo.copy(status = toDo.status.check()))
                    }
                })
            Text(
                text = toDo.title,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}