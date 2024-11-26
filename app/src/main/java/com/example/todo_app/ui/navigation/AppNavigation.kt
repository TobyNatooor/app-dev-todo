package com.example.todo_app.ui.navigation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_app.data.DataHandler
import com.example.todo_app.ui.feature.home.HomeScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListScreen
import com.example.todo_app.ui.theme.appBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val dataHandler = remember { DataHandler() }
    val appBar = @Composable { AppBar(navController) }


    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                dataHandler = dataHandler
            )
        }
        composable(
            route = "todoList/{title}/{listId}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("listId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: "Error"
            val listId = backStackEntry.arguments?.getInt("listId") ?: -1

            ToDoListScreen(
                title = title,
                listId = listId,
                appBar = appBar,
                dataHandler = dataHandler
            )
        }
    }
}