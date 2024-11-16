package com.example.todo_app.ui.feature.toDoList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

import com.example.todo_app.model.ToDo


@Composable
fun ToDoList(toDos: List<ToDo>, modifier: Modifier = Modifier) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = modifier
            .fillMaxSize()
    ) {
        itemsIndexed(toDos) { index, toDo ->
            ToDoItem(toDo = toDo, index = index)
        }
    }
}

@Composable
private fun ToDoItem(toDo: ToDo, index: Int = 0) {
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
        Text(
            text = toDo.title,
            fontSize = 20.sp,
            color = Color.Black
        )
    }
}