package com.example.todo_app

import com.example.todo_app.data.AppDatabase
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
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

        Log.d("TESTING", "xyz")
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "ToDoDB"
        ).build()

        Log.d("TESTING", "123")
        enableEdgeToEdge()
        lifecycleScope.launch(Dispatchers.IO) {
            // Should not be in release
            if (db.toDoDao().numberOfToDos() == 0) {
                MockDataStore().insertMockData(db)
                Log.d("TESTING", "onCreate: ${db.toDoDao().numberOfToDos()}")
            }
            withContext(Dispatchers.Main) {
                setContent {
                    TodoappTheme {
                        AppNavigation(db)
                    }
                }
            }
        }
        Log.d("TESTING", "abc")
    }
}
