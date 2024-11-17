package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.background
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModel

import com.example.todo_app.model.ToDo


@Composable
fun ToDoList(toDos: List<ToDo>, viewmodel: ToDoListViewModel, modifier: Modifier = Modifier, title: String = "") {
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
                    modifier = Modifier.fillMaxWidth().padding(top = 60.dp, bottom = 30.dp)
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp) // Inner padding for the content inside the box
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(toDo.isDone, onCheckedChange = {
                viewmodel.updateToDoItem(toDo.copy(isDone = !toDo.isDone))
            })
            Text(
                text = toDo.title,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
    }
}