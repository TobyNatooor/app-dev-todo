package com.example.todo_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_app.data.DataHandler
import com.example.todo_app.ui.feature.home.HomeScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListScreen

@Composable
fun AppNavigation(dataHandler : DataHandler) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val localDataHandler = remember { dataHandler }
            HomeScreen(
                navController = navController,
                dataHandler = localDataHandler
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

            val localDataHandler = remember { dataHandler }

            ToDoListScreen(
                title = title,
                listId = listId,
                dataHandler = localDataHandler
            )
        }
    }
}