package com.example.todo_app.data

class MockToDoDataStore {
    var todoData = listOf(
        ToDoDOA(1, "Buy Apples", false, "Pick up apples from the grocery store.", 1),
        ToDoDOA(2, "Buy Bananas", false, "Get bananas for the week.", 1),
        ToDoDOA(3, "Buy Oranges", false, "Buy a pack of fresh oranges.", 1),

        ToDoDOA(4, "Clean Kitchen Counter", false, "Wipe down all surfaces in the kitchen.", 2),
        ToDoDOA(5, "Clean Living Room Floor", false, "Vacuum the living room floor.", 2),
        ToDoDOA(6, "Clean Bathroom Sink", false, "Scrub the sink in the bathroom.", 2),

        ToDoDOA(7, "Run 5km", false, "Go for a 5km jog around the park.", 3),
        ToDoDOA(8, "Push-ups", false, "Do 20 push-ups.", 3),
        ToDoDOA(9, "Squats", false, "Complete 3 sets of 15 squats.", 3),

        ToDoDOA(10, "Review Meeting Agenda", false, "Go over the meeting agenda and notes.", 4),
        ToDoDOA(11, "Prepare Presentation Slides", false, "Finalize the slides for the client meeting.", 4),
        ToDoDOA(12, "Confirm Meeting Time", false, "Double-check the meeting time with the client.", 4),

        ToDoDOA(13, "Complete Practice Exam", false, "Finish the practice exam for the upcoming test.", 5),
        ToDoDOA(14, "Review Chapter 5", false, "Go over Chapter 5 for the study session.", 5),
        ToDoDOA(15, "Make Study Notes", false, "Create detailed notes for the next exam.", 5),

        ToDoDOA(16, "Start Laundry Load", false, "Put clothes in the washer.", 6),
        ToDoDOA(17, "Transfer Clothes to Dryer", false, "Move clothes from washer to dryer.", 6),
        ToDoDOA(18, "Fold Clothes", false, "Fold the clean laundry.", 6),

        ToDoDOA(19, "Schedule Appointment", false, "Call the dentist and book an appointment.", 7),
        ToDoDOA(20, "Confirm Insurance", false, "Ensure the insurance covers the dentist visit.", 7),
        ToDoDOA(21, "Prepare Dental Questions", false, "Make a list of questions for the dentist visit.", 7),

        ToDoDOA(22, "Prep Chicken for Cooking", false, "Marinate the chicken for dinner.", 8),
        ToDoDOA(23, "Cook Rice", false, "Cook the rice to go with dinner.", 8),
        ToDoDOA(24, "Steam Vegetables", false, "Steam some broccoli and carrots as a side.", 8)
    )

}