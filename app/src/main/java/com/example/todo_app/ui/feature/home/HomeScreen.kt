package com.example.todo_app.ui.feature.home

import android.content.Intent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo_app.data.DataHandler
import com.example.todo_app.model.CheckList
import com.example.todo_app.ui.list.ListActivity

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    dataHandler: DataHandler
) {
    val lists = remember { mutableStateListOf<CheckList>() }
    lists.clear()
    lists.addAll(dataHandler.getCheckLists())

    Scaffold(
        floatingActionButton = {
            ListButton(lists, dataHandler)
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(horizontal = 40.dp),
            modifier = modifier.padding(paddingValues)
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
                ListCard(lists[index], navController)
            }
        }
    }
}

@Composable
fun ListCard(list: CheckList, navController: NavController) {
    val context = LocalContext.current
    return Card(
        onClick = {
            navController.navigate("todoList/${list.title}/${list.id}")
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
            // TODO: refactor
            /*
            var index = 0
            while (index < 3 && index < list.toDos.size) {
                Text(list.toDos[index].title)
                index++
            }
            */
        }
    }
}

@Composable
fun ListButton(lists: MutableList<CheckList>, dataHandler: DataHandler) {
    FloatingActionButton(
        onClick = {
            val newListTitle = dataHandler.createNewListName(lists)
            val newList = CheckList(
                id = dataHandler.newListId(),
                title = newListTitle,
                description = ""
            )
            lists.add(newList)
            dataHandler.save(lists)
        },
        // Remove shape parameter for default shape (square with rounded corners)
        shape = RoundedCornerShape(45, 45, 45, 45),
        modifier = Modifier.padding(20.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add new list", tint = Color.Black)
    }
}