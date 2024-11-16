package com.example.todo_app.data.mock

import com.example.todo_app.data.model.ToDoDAO

class MockToDoDataStore {
    val todoData = listOf(
        ToDoDAO(1, "Buy Apples", false, "Pick up apples from the grocery store.", 1),
        ToDoDAO(2, "Buy Bananas", false, "Get bananas for the week.", 1),
        ToDoDAO(3, "Buy Oranges", false, "Buy a pack of fresh oranges.", 1),

        ToDoDAO(4, "Clean Kitchen Counter", false, "Wipe down all surfaces in the kitchen.", 2),
        ToDoDAO(5, "Clean Living Room Floor", false, "Vacuum the living room floor.", 2),
        ToDoDAO(6, "Clean Bathroom Sink", false, "Scrub the sink in the bathroom.", 2),

        ToDoDAO(7, "Run 5km", false, "Go for a 5km jog around the park.", 3),
        ToDoDAO(8, "Push-ups", false, "Do 20 push-ups.", 3),
        ToDoDAO(9, "Squats", false, "Complete 3 sets of 15 squats.", 3),

        ToDoDAO(10, "Review Meeting Agenda", false, "Go over the meeting agenda and notes.", 4),
        ToDoDAO(11, "Prepare Presentation Slides", false, "Finalize the slides for the client meeting.", 4),
        ToDoDAO(12, "Confirm Meeting Time", false, "Double-check the meeting time with the client.", 4),

        ToDoDAO(13, "Complete Practice Exam", false, "Finish the practice exam for the upcoming test.", 5),
        )
}