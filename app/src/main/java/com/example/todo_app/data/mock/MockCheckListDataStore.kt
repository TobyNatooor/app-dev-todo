package com.example.todo_app.data.mock

import com.example.todo_app.data.model.CheckListDAO
import com.example.todo_app.model.ToDo

class MockCheckListDataStore {

    val checklistData = listOf(
        CheckListDAO(1, "Grocery Shopping", "Make sure to buy fruits, vegetables, and snacks for the week."),
        CheckListDAO(2, "Home Cleaning", "Clean the kitchen, living room, and bathroom."),
        CheckListDAO(3, "Workout Routine", "Complete 30 minutes of cardio followed by strength training."),
        CheckListDAO(4, "Work Meeting", "Prepare for the client meeting by reviewing the presentation."),
        CheckListDAO(5, "Study Session", "Study for the upcoming exam and complete practice problems."),
        CheckListDAO(6, "Laundry", "Wash, dry, and fold clothes."),
        CheckListDAO(7, "Book Appointment", "Schedule a dentist visit for a checkup."),
        CheckListDAO(8, "Dinner Preparation", "Cook a healthy dinner with chicken, rice, and vegetables.")
    )
}