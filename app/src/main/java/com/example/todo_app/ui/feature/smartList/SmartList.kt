package com.example.todo_app.ui.feature.smartList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todo_app.model.ToDo

@Composable
fun SmartList(
    toDos: List<ToDo>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(toDos) { toDo ->
            Column {
                Text(text = toDo.title)
                Text(text = toDo.description)
            }
        }
    }
}
