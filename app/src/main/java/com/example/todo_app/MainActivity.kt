package com.example.todo_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.repository.MockCheckListDataStore
import com.example.todo_app.ui.list.ListActivity
import com.example.todo_app.ui.theme.TodoappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoappTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomePage(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HomePage(modifier: Modifier) {
    val lists = MockCheckListDataStore().getLists()

    return LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(40.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        contentPadding = PaddingValues(horizontal = 40.dp),
        modifier = modifier.padding(bottom = 40.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                "My Lists",
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp, bottom = 30.dp)
            )
        }
        items(lists.size) { index ->
            ListCard(lists[index])
        }
    }
}

@Composable
fun ListCard(list: CheckList) {
    val context = LocalContext.current
    return Card(
        onClick = {
            val intent = Intent(context, ListActivity::class.java)
                .putExtra("TITLE", list.title)
            context.startActivity(intent)
        },
        modifier = Modifier
            .width(130.dp)
            .height(180.dp)
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
            var index = 0
            while (index < 5 && index < list.toDos.size) {
                Text(list.toDos[index].title)
                index++
            }
        }
    }
}
