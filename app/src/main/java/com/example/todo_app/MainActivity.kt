package com.example.todo_app

import android.app.Activity
import com.example.todo_app.data.AppDatabase
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.todo_app.BuildConfig.MAPS_API_KEY
import com.example.todo_app.data.mock.MockDataStore
import com.example.todo_app.ui.navigation.AppNavigation
import com.example.todo_app.ui.theme.TodoappTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private var locationActivityCallback: ((Place?) -> Unit)? = null

    private val findingLocationActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultData = result.data
                if (resultData != null) {
                    val place = Autocomplete.getPlaceFromIntent(resultData)
                    locationActivityCallback?.invoke(place)
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                locationActivityCallback?.invoke(null)
            }
        }

    private fun getLocation(callback: ((Place?) -> Unit?)) {
        val fields = listOf(Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.LOCATION)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)
        locationActivityCallback = { place ->
            callback(place)
            Log.d("PLACEPLACE", "$place")
        }
        findingLocationActivity.launch(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the Places API with the API key
        Places.initialize(applicationContext, MAPS_API_KEY)

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
                        AppNavigation(db, ::getLocation)
                    }
                }
            }
        }
        Log.d("TESTING", "abc")
    }
}
