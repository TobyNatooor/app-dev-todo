# Iterration 2

## Task 1: Create project
### Covers no user story
### Discription
#### Part 1
Create kotlin project and reporsitiry. The repository should be on github and shared with the team. The kotlin project should be able to run and display an empty page, this page will be called the home page.
#### Part 2
Data models for lists and TODOs should be defined, List should have a name, links to its TODOs and other relevant information. TODO should have a description, status and other relevant information. 
#### Part 3
A class called data should be made, this class should have funtions to create and return lists and TODOs in memory as objects. A few example lists and TODOs should be hardcoded to be saved into memory (this will later be removed).

## Task 2: Create list (dependeci on task 1)
### Covers user story 1
### Discription
On the home page, should be a round button in the bottem right cornor that creates a list. When the button is pressed a list should be saved to memory with the name "new list"+i, where i is a number not yet used in a list name. Saving the list to memory should be handled by a seprate class called data.

## Task 3: Display lists (dependeci on task 1)
### Covers user story 2 
### Discription
#### Part 1
The home page should display any lists stored in memory as a rounded squre with it's name written on it. The lists in memory should be managed by a seprate class called data. The lists should be in a grid of 2x6. The lists should be scrollable, making the grid size essentialy 2xUnlimited.
#### Part 2
When a list is pressed a new page should apper, this page represents the contents of the list. The content page should have the name of the list in the top and the rest of the page empty. Make sure to also include a back button to return to the home page.

## Task 4: Create TODO (dependeci on task 3)
### Covers user story 6
### Discription
The page of a list's content should have a button that creates a TODO. When the button is pressed a TODO should be saved to memory with the name "new TODO"+i, where i is a number not yet used in a list name. Saving the TODO to memory should be handled by the data class.

## Task 5: Display TODO (dependeci on task 3)
### Covers user story 7
### Discription
In any list's content page should any TODO of that list be displayed. The TODOs should be displayed as its discription text in a list format. (The TODOs of any list is saved in the data of the list object).

## Task 6: Mark TODO as done (dependeci on task 5)
### Covers user story 0
### Discription
Any TODO diplayed in a list content page should have a squre to the left of the discription. This squre should have two visual stages (done : not done). The stage should be defined by the TODO objects staus field. when pressing the squre the TODO object should change status and in turn change the UI squre.

# Iterration 3

## Task 15: Data (dependeci on task 5)
### Covers user story 0
#### Discription
Note: a list without a parent folder will be refered to as a naked list. 
Create a working data base for local storage on device an include the feture of folders. The database can be designed however deemed fit by the developor but need to fufill the following needs from the app.
1. The app should be able to get all data needed to display all folders and all naked lists.
2. The app should be able to get all data needed to display all lists inside a given folder.
3. The app should be able to get all data needed to display all todo's inside a given list.
4. The app should be able to get all data needed to display all the settings of a folder/list/todo (eg. location, deadline, name and so on)
5. The app should be able to add elements to the database.
6. The app should be able to modify settings in an element.
7. The app should be able to, from a sring get all lists and folders with a name containing that string.
8. The app should be able to, from a sring get all todos inside a given list with a name/description containing that string.

## Task 9: Separate TODO (dependeci on task 6)
### Covers user story 11
### Discription
When on a list's content page, the TODOs should be separated based on status. All the TODOs with a "not done" status should appere before any TODOs with a "done" status.

# Iterration 4

## Task 14: Seach (dependenci on task 15)
### Covers user story 17 and 18
### Discription
#### Part 1
A seach sign should be shown on top of the list view and on top of the todo view (see prototype for refrence). When pressed, a seach bar should appere prompting the phone keybored to appear. This seach bar is not a new page but an overlay on top of the home page or list page depending from where it was pressed.
#### Part 2
If the seach button is pressed on the home page, the elements displayed on the home page should now be elements of the seach result. The seach results should be any list (naked or not) or folder with a name containg the seach string.
#### Part 3
If the seach button is pressed on a list page, the todo elements displayed on that page should noe be the elements of the seach results. The seach results should be any todo inside of that list, with a name/description containg the seach string.

## Task 7: Naming lists and TODOs (dependeci on task 2, 3, 4 and 5)
### Covers no user story
### Discription
#### Part 1
When the create list button is pressed (task 2), the view should be updated to include the newly made list.
#### Part 2
When a newly created list shows on the home page the name showed on the list should funtion as a editble text field. That text field should be in focus, promting the phone keybord to pop up and be able to edit the name. When the text field no longer is in focus (fx if the user presses enter or presses anywhere else on the screen) the name of the list should be updated to the text in the text field. To sum up the UX flow:
1. The user presses create list, a new list apperes on screen.
2. The keybord pops up and is able to edit the name.
3. The user types the prefered name.
4. The user presses enter or some where on the home page.
5. The keybord disaperes and the name of the list is updated.
#### Part 3 (repeat part 1 for TODOs)
When the create TODO button is pressed (task 4), the view should be updated to include the newly made TODO.
#### Part 4 (repeat part 2 for TODOs)
When a newly created TODO shows on the home page the discription should funtion as a editble text field. That text field should be in focus, promting the phone keybord to pop up and be able to edit the discription. When the text field no longer is in focus (fx if the user presses enter or presses anywhere else on the screen) the discription of the TODO should be updated to the text in the text field. To sum up the UX flow:
1. The user presses create TODO, a new TODO apperes on screen.
2. The keybord pops up and is able to edit the discription.
3. The user types the prefered discription.
4. The user presses enter or some where on the list content page.
5. The keybord disaperes and the discription of the TODO is updated.

## Task 8: Update list and TODO (dependeci on task 3 and 5)
### Covers user story 3 and 8
### Discription
#### Part 1
On the home page each list should have three vertical dots in thier top right cornor, that when pressed takes you to a page representing the settings for that list.
#### Part 2
When on a lists content page there should be three vertical dots in the top right conor of the page, that when pressed takes you to a page representing the settings for that list.
#### Part 3
The three dots from part 1 and 2 both takes you to the same page. This page is the settings of the list. From here it should be posible to edit the name of the list (make room for furture settings). Make sure to include a back button to return to previus page.
#### Part 4
When on a lists content page, each TODO should have three vertical dots on the right side of the discription. These dots, when pressed should take you to a new page representing the settings of that TODO.
#### Part 5 (repeat part 3 for TODOs)
The three dots from part 4 takes you to a settings page of the TODO. From here it should be posible to edit the discription of the TODO (make room for furture settings). Make sure to include a back button to return to previus page.

## Task 16: Favorites (dependeci on task 8 and 14)
### Covers user story 28
### Discription
#### Part 1
Update the database to include a boolean for each list, denoting if that list is selected as a favorite or not.
#### Part 2
Update the settings page to include the option to add/remove that list as a favorite.
#### Part 3
On a list page, add a star icon in the top right cabable of two states, outline and filled in. Pressing this star will couse the icon to change between the two states along with updateing the lists favorite status (see figma).
#### Part 4 
If more than zero lists are marked as favorite, they should on the home page be shown above any list or buttons (above seach icon). A star should also be displayed before the favorite lists (see figma).

## Task 17: Create new (dependeci on task 7)
### Discription
#### Part 1
On the home page, the big plus button should now change behaiver. When pressed it should reveal two new buttons above it along with text for each one (see figma). The text and colors of the new elements dosnt have to be the same as in figma, if you come op with better ideas do share them.
#### Part 2
The button right above the original one should act exactly the same as the original one used to act (creating a new list and prompting keybord) make sure to reuse the code.
#### Part 3
The top button should take you to a new page. This page should have a text field the user can write in and a create button that creates a list with the name in the text field and takes the user back to the main page (see figma) (Make room for more options in the furture where th user can add collaboraters and create folders and more).

## Task 18: Sort (dependeci on task 16)
### Covers user story 29
### Discription
#### Part 1
The database should be modified to log the time of creation and the time of last modification for every list and task. The database should be able to suply the lists and tasks (sepretly) ordered alphabetacle, by date of creation and by date of last modified. Any list marked as favorite should not be included in the sorting but be returned sepretly.
### Part 2
Next to the seach icon on the home page, there should be a sorting icon that when pressed displayes a dropdown menue. The dropdown menue consists of a header and three text elements, the header should read "sort by" and the three options should be "Title", "Date modified" and "Date created". When one is selected the lists should be sorted acordingly besides. Remeber that the if any list is marked as favorite they should be displayed above the rest (see figma).
### Part 3
If the lists are sorted by the title, the lists should have indicators displayed above them if they are the first list in the order with the first letter that they have (see figma).
### Part 4
When scrolling trough the lists, a scroll bar shold appear on the right side letting the user quicly move trhough the lists. If the user drags the scroll bar a text element should apper next to it, displaying the letter or date (depending on the selecting sorting) of the list on the screen.
### Part 5
Repeat part 2, 3 and 4 but for the tasks inside a lists page.

# Iterration 5

## Task 10: More TODO settings (dependeci on task 15)
### Covers user story 4, 9, 10, 12, 13, 14, 15 and 16
### Discription
#### Part 1
The data model for a TODO should be updated to include the following information.
1. start (the date and time this TODO should start)
2. deadline (the date and time this TODO should end)
3. time (the time it takes to do this TODO)
4. dependeci (a list of pointers to any TODO that should be done before this one)
5. recuring (an array booleans with length 7 representing wether the TODO should repeat that day of the week)
6. status (this field already exist but should be updated to inklude the status "in progress", "canceled" and "cant be done")
#### Part 2
In the settings page of a TODO there should be added ways to edit the fields from part 1, exept the status field. In addition to that, there should be a button to delete the TODO (and a warning pop up before deletion).

## Task 11: More TODO status (dependeci on task 10)
### Covers user story 10 and 16
### Discription
#### Part 1
The status squre of a TODO should be able to display 5 diffrent statuses.
1. not done (white squre)
2. done (green squre with a lighter shade of green checkmark)
3. in progress (yellow squre with a darker shade of yellow clock icon)
4. canceled (red squre with a lighter shade of red "X")
5. cant be done (gray squre)
#### Part 2
When long pressing on the squre a small pop up should appere showing the four status options ("cant be done" is not included in the pop up). If one of those squres is pressed, the status of that TODO will be changed to that status.
#### Part 3
If a TODO has dependenci on one or more TODOs that dosnt have the status "done", the status of that TODO should be changed to "cant be done". If a TODO has the status "cant be done", pressing or long pressing on that squre wont do anything.

# Placeholder

## Task 12: Createing (dependeci on task 5)
### Covers user story 27
### Discription
A plus sign should be shown on top of the list view and on top of the todo view (see prototype for refrence), this plus sign when pressed should take you to a new page. From this page it should be posible to create a new folder, list or todo.
1. To create a folder the user needs to specify the name of the folder.
2. To create a list the user needs to specify the name of the list and optianally choose an existing folder as the lists parrent folder.
3. To create a todo the user needs to specify the name of the todo and chose an existing list as the todos parent list (not optional).

## Task 13: Folders (dependeci on task 15)
### Covers user story 27
### Discription
#### Part 1
Display folders on the home page alongside lists (see prototype for refrence), make sure only naked lists are shown alongside lists.
#### Part 2
When a folder is pressed, display the lists of that folder as a pop up (see prototype for refrence).

# User storys not yet covered
15 todo recuring
19 smart list
23 login
24 collab list
25 assign todo
20 notification
21 theme
22 export todo
5 background
26 sync with google calender