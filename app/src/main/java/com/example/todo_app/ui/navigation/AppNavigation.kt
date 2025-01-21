package com.example.todo_app.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import com.example.todo_app.data.AppDatabase
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todo_app.model.SortOption
import com.example.todo_app.ui.feature.home.HomeScreen
import com.example.todo_app.ui.feature.home.HomeViewModel
import com.example.todo_app.ui.feature.home.HomeViewModelFactory
import com.example.todo_app.ui.feature.smartList.SmartListScreen
import com.example.todo_app.ui.feature.toDoOptions.ToDoOptionsScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListScreen
import com.example.todo_app.ui.feature.toDoList.ToDoListViewModel
import com.example.todo_app.ui.feature.toDoList.ToDoListViewModelFactory
import com.google.android.libraries.places.api.model.Place

@Composable
fun AppNavigation(
    db: AppDatabase,
    getLocation: ((Place?) -> Unit?) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
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
                        sortOptions = listOf(SortOption.NAME, SortOption.CREATED, SortOption.RECENT)
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
            val viewmodel: ToDoListViewModel = viewModel(
                key = "ToDoListViewModel_$listId",
                factory = ToDoListViewModelFactory(listId, db, navController)
            )
            ToDoListScreen(
                title = title,
                viewmodel = viewmodel,
                listId = listId,
                appBar = @Composable {
                    AppBar(
                        actions = listOf(
                            AppBarAction.Back,
                            AppBarAction.Sort,
                            AppBarAction.Search
                        ),
                        onSortClicked = { option -> viewmodel.sortToDos(option) },
                        onBackClicked = { navController.popBackStack() },
                        sortOptions = listOf(SortOption.NAME, SortOption.CREATED, SortOption.RECENT, SortOption.STATUS)
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
                navController = navController,
                appBar = @Composable {
                    AppBar(
                        actions = listOf(AppBarAction.Back),
                        onBackClicked = { navController.popBackStack() },
                        sortOptions = emptyList()
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
                        sortOptions = emptyList()
                    )
                }
            )
        }
    }
}
