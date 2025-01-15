package com.example.todo_app.ui.navigation

import com.example.todo_app.data.AppDatabase
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_app.ui.feature.home.HomeScreen
import com.example.todo_app.ui.feature.toDoOptions.ToDoOptionsScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListScreen

@Composable
fun AppNavigation(db: AppDatabase) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                db = db,
                navController = navController,
                appBar = @Composable { AppBar(navController, backButton = false, sortButton = true, searchButton = true) }
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
                appBar = @Composable { AppBar(navController, backButton = true, sortButton = true, searchButton =  true) },
                db = db,
                navController = navController
            )
        }
        composable(
            route = "toDoOptions/{toDoId}",
            arguments = listOf(
                navArgument("toDoId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val toDoId = backStackEntry.arguments?.getInt("toDoId") ?: -1

            ToDoOptionsScreen(
                toDoId = toDoId,
                appBar = @Composable { AppBar(navController, backButton = true, sortButton = false, searchButton =  false) },
                db = db
            )
        }
    }
}
