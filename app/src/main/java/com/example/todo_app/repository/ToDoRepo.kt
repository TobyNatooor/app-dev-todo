package com.example.todo_app.repository

import com.example.todo_app.data.mock.MockToDoDataStore
import com.example.todo_app.model.ToDo

class ToDoRepo {
    val datasource = MockToDoDataStore();

    val toDos = listOf(
        ToDo("Buy Apples", false, "Pick up apples from the grocery store."),
        ToDo("Buy Bananas", false, "Get bananas for the week."),
        ToDo("Buy Oranges", false, "Buy a pack of fresh oranges."),

        ToDo("Clean Kitchen Counter", false, "Wipe down all surfaces in the kitchen."),
        ToDo("Clean Living Room Floor", false, "Vacuum the living room floor."),
        ToDo("Clean Bathroom Sink", false, "Scrub the sink in the bathroom."),

        ToDo("Run 5km", false, "Go for a 5km jog around the park."),
        ToDo("Push-ups", false, "Do 20 push-ups."),
        ToDo("Squats", false, "Complete 3 sets of 15 squats."),

        ToDo("Review Meeting Agenda", false, "Go over the meeting agenda and notes."),
        ToDo("Prepare Presentation Slides", false, "Finalize the slides for the client meeting."),
        ToDo("Confirm Meeting Time", false, "Double-check the meeting time with the client."),

        ToDo("Complete Practice Exam", false, "Finish the practice exam for the upcoming test."),
        ToDo("Review Chapter 5", false, "Go over Chapter 5 for the study session."),
        ToDo("Make Study Notes", false, "Create detailed notes for the next exam."),

        ToDo("Start Laundry Load", false, "Put clothes in the washer."),
        ToDo("Transfer Clothes to Dryer", false, "Move clothes from washer to dryer."),
        ToDo("Fold Clothes", false, "Fold the clean laundry."),

        ToDo("Schedule Appointment", false, "Call the dentist and book an appointment."),
        ToDo("Confirm Insurance", false, "Ensure the insurance covers the dentist visit."),
        ToDo("Prepare Dental Questions", false, "Make a list of questions for the dentist visit."),

        ToDo("Prep Chicken for Cooking", false, "Marinate the chicken for dinner."),
        ToDo("Cook Rice", false, "Cook the rice to go with dinner."),
        ToDo("Steam Vegetables", false, "Steam some broccoli and carrots as a side.")
    )

}