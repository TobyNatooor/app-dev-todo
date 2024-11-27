package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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

@Composable
fun HomeList(
    lists: List<CheckList>,
    viewModel: HomeViewModel,
){

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        contentPadding = PaddingValues(horizontal = 40.dp),
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                "My Lists",
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 100.dp, bottom = 100.dp)
            )
        }
        items(lists.size) { index ->
            ListCard(lists[index], viewModel)
        }
    }
}

@Composable
private fun ListCard(list: CheckList, viewModel: HomeViewModel) {
    return Card(
        onClick = {
            viewModel.clickList(listTitle = list.title, listId = list.id)
        },
        modifier = Modifier
            .width(130.dp)
            .height(130.dp)
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
        }
    }
}