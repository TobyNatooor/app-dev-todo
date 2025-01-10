package com.example.todo_app.data.mock

import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo

class MockDataStore {
    private val todoData = listOf(
        ToDo(title = "Buy Apples", description = "Pick up apples from the grocery store.", listId = 1, order = 1),
        ToDo(title = "Buy Bananas", description = "Get bananas for the week.", listId = 1, order = 2),
        ToDo(title = "Buy Oranges", description = "Buy a pack of fresh oranges.", listId = 1, order = 3),

        ToDo(title = "Clean Kitchen Counter", description = "Wipe down all surfaces in the kitchen.", listId = 2, order = 1),
        ToDo(title = "Clean Living Room Floor", description = "Vacuum the living room floor.", listId = 2, order = 2),
        ToDo(title = "Clean Bathroom Sink", description = "Scrub the sink in the bathroom.", listId = 2, order = 3),
        ToDo(title = "Clean Bathroom Floor", description = "Scrub the floor in the bathroom.", listId = 2, order = 4),
        ToDo(title = "Clean Bathroom Walls", description = "Scrub the walls in the bathroom.", listId = 2, order = 5),

        ToDo(title = "Run 5km", description = "Go for a 5km jog around the park.", listId = 3, order = 1),
        ToDo(title = "Push-ups", description = "Do 20 push-ups.", listId = 3, order = 2),
        ToDo(title = "Squats", description = "Complete 3 sets of 15 squats.", listId = 3, order = 3),

        ToDo(title = "Review Meeting Agenda", description = "Go over the meeting agenda and notes.", listId = 4, order = 1),
        ToDo(title = "Prepare Presentation Slides", description = "Finalize the slides for the client meeting.", listId = 4, order = 2),
        ToDo(title = "Confirm Meeting Time", description = "Double-check the meeting time with the client.", listId = 4, order = 3),

        ToDo(title = "Complete Practice Exam", description = "Finish the practice exam for the upcoming test.", listId = 5, order = 1),
        ToDo(title = "Review Chapter 5", description = "Go over Chapter 5 for the study session.", listId = 5, order = 2),
        ToDo(title = "Make Study Notes", description = "Create detailed notes for the next exam.", listId = 5, order = 3),

        ToDo(title = "Start Laundry Load", description = "Put clothes in the washer.", listId = 6, order = 1),
        ToDo(title = "Transfer Clothes to Dryer", description = "Move clothes from washer to dryer.", listId = 6, order = 2),
        ToDo(title = "Fold Clothes", description = "Fold the clean laundry.", listId = 6, order = 3),

        ToDo(title = "Schedule Appointment", description = "Call the dentist and book an appointment.", listId = 7, order = 1),
        ToDo(title = "Confirm Insurance", description = "Ensure the insurance covers the dentist visit.", listId = 7, order = 2),
        ToDo(title = "Prepare Dental Questions", description = "Make a list of questions for the dentist visit.", listId = 7, order = 3),

        ToDo(title = "Prep Chicken for Cooking", description = "Marinate the chicken for dinner.", listId = 8, order = 1),
        ToDo(title = "Cook Rice", description = "Cook the rice to go with dinner.", listId = 8, order = 2),
        ToDo(title = "Steam Vegetables", description = "Steam some broccoli and carrots as a side.", listId = 8, order = 3)
    )

    private val listData = listOf(
        CheckList(
            title = "Grocery Shopping",
            description = "Make sure to buy fruits, vegetables, and snacks for the week.",
            order = 1,
            folderId = 1
        ),
        CheckList(
            title = "Home Cleaning",
            description = "Clean the kitchen, living room, and bathroom.",
            order = 2,
            folderId = 1
        ),
        CheckList(
            title = "Workout Routine",
            description = "Complete 30 minutes of cardio followed by strength training.",
            order = 3,
            folderId = 1
        ),
        CheckList(
            title = "Work Meeting",
            description = "Prepare for the client meeting by reviewing the presentation.",
            order = 4,
            folderId = 1
        ),
        CheckList(
            title = "Study Session",
            description = "Study for the upcoming exam and complete practice problems.",
            order = 5,
            folderId = 1
        ),
        CheckList(
            title = "Laundry",
            description = "Wash, dry, and fold clothes.",
            order = 6,
            folderId = 1
        ),
        CheckList(
            title = "Book Appointment",
            description = "Schedule a dentist visit for a checkup.",
            order = 7,
            folderId = 1
        ),
        CheckList(
            title = "Dinner Preparation",
            description = "Cook a healthy dinner with chicken, rice, and vegetables.",
            order = 8,
            folderId = 1
        )
    )

    suspend fun insertMockData(db: AppDatabase) {
        db.checkListDao().insert(listData)
        db.toDoDao().insert(todoData)
    }
}