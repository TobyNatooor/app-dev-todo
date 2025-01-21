package com.example.todo_app.ui.navigation

import com.example.todo_app.data.AppDatabase
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_app.ui.feature.home.HomeScreen
import com.example.todo_app.ui.feature.home.HomeViewModel
import com.example.todo_app.ui.feature.home.HomeViewModelFactory
import com.example.todo_app.ui.feature.smartList.SmartListScreen
import com.example.todo_app.ui.feature.toDoOptions.ToDoOptionsScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListScreen
import com.example.todo_app.ui.theme.*
import com.google.android.libraries.places.api.model.Place

@Composable
fun AppNavigation(
    db: AppDatabase,
    getLocation: ((Place?) -> Unit?) -> Unit,
    buttonColor: Color = neutral1
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val viewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(db, navController)
            )
            HomeScreen(
                db = db,
                navController = navController,
                appBar = @Composable {
                    AppBar(
                        actions = listOf(
                            AppBarAction.Sort,
                            AppBarAction.Search
                        ),
                        onSortClicked = { option -> viewModel.sortLists(option) },
                        onSearchClicked = { query -> viewModel.searchForTodos(query) },
                        buttonColor = buttonColor
                    )
                }
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
                appBar = @Composable {
                    AppBar(
                        actions = listOf(
                            AppBarAction.Back,
                            AppBarAction.Sort,
                            AppBarAction.Search
                        ),
                        onBackClicked = { navController.popBackStack() },
                        buttonColor = buttonColor
                        )
                },
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
                appBar = @Composable {
                    AppBar(
                        actions = listOf(AppBarAction.Back),
                        onBackClicked = { navController.popBackStack() },
                        buttonColor = buttonColor
                    )
                },
                getLocation = getLocation,
                db = db
            )
        }
        composable("smartList") {
            SmartListScreen(
                db = db,
                navController = navController,
                appBar = @Composable {
                    AppBar(
                        actions = listOf(AppBarAction.Back),
                        onBackClicked = { navController.popBackStack() },
                        buttonColor = buttonColor
                    )
                }
            )
        }
    }
}
