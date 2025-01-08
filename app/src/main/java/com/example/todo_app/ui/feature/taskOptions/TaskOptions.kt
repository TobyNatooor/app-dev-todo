package com.example.todo_app.ui.feature.taskOptions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.ToDo

@Composable
fun TaskOptions(
    task: ToDo,
    viewmodel: TaskOptionsViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

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
                text = task.title.toString(),
                textAlign = TextAlign.Center,
                style = TextStyle(fontSize = 60.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 75.dp, bottom = 75.dp)
            )
        }
    }
}