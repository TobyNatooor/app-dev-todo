package com.example.todo_app.ui.navigation

import AppDatabase
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_app.ui.feature.home.HomeScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListScreen

@Composable
fun AppNavigation(db: AppDatabase) {
    val navController = rememberNavController()
    val appBar = @Composable { AppBar(navController) }


    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                navController = navController,
                db = db
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
                db = db
            )
        }
    }
}