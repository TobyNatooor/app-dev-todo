package com.example.todo_app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.repository.MockCheckListDataStore

@Composable
fun ListCard(list: CheckList) {
    return Card(
        modifier = Modifier
            .width(130.dp)
            .height(110.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Row {
                Text(list.title, fontSize = 20.sp)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.Rounded.MoreVert,
                    contentDescription = null,
                )
            }
            if (list.toDos.isNotEmpty()) {
                Text(list.toDos[0].title)
            }
            if (list.toDos.size > 1) {
                Text(list.toDos[1].title)
            }
            if (list.toDos.size > 2) {
                Text(list.toDos[2].title)
            }
        }
    }
}

@Composable
fun ListGrid() {
    val lists = MockCheckListDataStore().getLists()

    return LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier.padding(20.dp),
    ) {
        items(lists.size) { index ->
            ListCard(lists[index])
        }
    }
}

@Composable
fun HomePage(modifier: Modifier) {
    return Column(modifier = modifier) {
        Text(
            "My Lists",
            textAlign = TextAlign.Center,
            fontSize = 60.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
        )
        ListGrid()
    }
}
