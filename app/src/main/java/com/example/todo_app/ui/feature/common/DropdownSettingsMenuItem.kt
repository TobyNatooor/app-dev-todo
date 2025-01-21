package com.example.todo_app.ui.feature.common

sealed class DropdownSettingsMenuItem {
    object Share : DropdownSettingsMenuItem()
    object Edit : DropdownSettingsMenuItem()
    object Rename : DropdownSettingsMenuItem()
    object Favorite : DropdownSettingsMenuItem()
    object Merge : DropdownSettingsMenuItem()
    object Delete : DropdownSettingsMenuItem()
}