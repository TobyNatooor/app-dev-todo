package com.example.todo_app.ui.navigation

sealed class AppBarAction {
    object Back : AppBarAction()
    object Sort : AppBarAction()
    object Search : AppBarAction()
}