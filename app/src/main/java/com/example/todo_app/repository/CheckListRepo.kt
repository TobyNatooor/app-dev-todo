package com.example.todo_app.repository

import com.example.todo_app.model.CheckList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CheckListRepo {
    private val dataSoruce = MockCheckListDataStore();
}