package com.example.todo_app

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.todo_app.repository.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "setting"
)

class MyApplication: Application() {
    lateinit var userRepository: UserRepository

    override fun onCreate(){
        super.onCreate()
        userRepository = UserRepository(dataStore)
    }
}