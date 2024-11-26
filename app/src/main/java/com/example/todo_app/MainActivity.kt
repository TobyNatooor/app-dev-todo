package com.example.todo_app

import AppDatabase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.todo_app.data.mock.MockDataStore
import com.example.todo_app.ui.navigation.AppNavigation
import com.example.todo_app.ui.theme.TodoappTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ToDoDB"
        ).build()

//        lifecycleScope.launch {
//            MockDataStore().insertMockData(db)
//        }
        enableEdgeToEdge()

        setContent {
            TodoappTheme {
                val navController = rememberNavController()
                    AppNavigation(db)
                }
            }
        }
}

