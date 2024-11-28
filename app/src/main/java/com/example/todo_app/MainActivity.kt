package com.example.todo_app

import com.example.todo_app.data.AppDatabase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.todo_app.data.mock.MockDataStore
import com.example.todo_app.ui.navigation.AppNavigation
import com.example.todo_app.ui.theme.TodoappTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // UNCOMMENT WHEN TESTING
        //applicationContext.deleteDatabase("ToDoDB")

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ToDoDB"
        ).build()


        enableEdgeToEdge()
        lifecycleScope.launch(Dispatchers.IO) {
            // Should not be in release
            if(db.toDoDao().numberOfToDos() == 0) {
                MockDataStore().insertMockData(db)
            }
            withContext(Dispatchers.Main){
                setContent {
                    TodoappTheme {
                        val navController = rememberNavController()
                        AppNavigation(db)
                    }
                }
            }
            }
        }


}

