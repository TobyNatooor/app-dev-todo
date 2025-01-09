package com.example.todo_app.ui.feature.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.substring
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu
import com.example.todo_app.model.SortOption
import com.example.todo_app.ui.theme.*

import com.example.todo_app.model.ToDo

@Composable
fun HomeList(
    lists: List<CheckList>,
    viewModel: HomeViewModel,
    gridState: LazyGridState
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var currentLetter: Char? = 'A'

    val textState = remember { mutableStateOf("") }

    val horizontalPadding = 40.dp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Title
            Text(
                "My Lists",
                textAlign = TextAlign.Center,
                fontSize = 54.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 100.dp, bottom = 80.dp)
            )

            // Search bar and sort button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = horizontalPadding / 2, end = horizontalPadding / 4
                    ),
                verticalAlignment = Alignment.Bottom
            ) {
                val childrenHeight = 42.dp
                val horizontalDistribution = 8f/15f

                SearchTextField(
                    viewModel,
                    focusRequester,
                    textState,
                    modifier = Modifier
                        .weight(horizontalDistribution)
                        //.border(1.dp, Color.Red)
                        .height(childrenHeight)
                )

                SortButton(
                    viewModel,
                    modifier = Modifier
                        .weight(1f - horizontalDistribution)
                        //.border(1.dp, Color.Blue)
                        .height(childrenHeight)
                )
            }

            // Lists
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalArrangement = Arrangement.spacedBy(40.dp),
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = 4.dp
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (lists.isEmpty()) {
                    item {
                        Text(
                            text = "No checklists found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    viewModel.currentChar = '\u0000'
                    items(lists.size) { index ->
                        Column {
                            if(viewModel.sortedOption == SortOption.NAME){
                                val char = viewModel.isNextChar(lists[index])
                                if (char != '!') {
                                    Text(
                                        char.toString(),
                                        style = TextStyle(fontSize = 13.sp),
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .width(15.dp)
                                            .height(4.dp)
                                    )
                                } else {
                                    // Space instead of text
                                    Spacer(modifier = Modifier.height(19.dp))
                                }
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                            ListCard(lists[index], textState.value, viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    viewModel: HomeViewModel,
    focusRequester: FocusRequester,
    textState: MutableState<String>,
    modifier: Modifier = Modifier
) {
    //val textState = remember { mutableStateOf("") }
    val focusState = remember { mutableStateOf(false) }

    val onFocusChange: (Boolean) -> Unit = { isFocused ->
        focusState.value = isFocused
    }

    Box(
        modifier = modifier
            .padding(end = 0.dp)
    ) {
        // Search TextField
        BasicTextField(
            value = textState.value,
            onValueChange = {
                textState.value = it
                viewModel.searchTodos(textState.value)
                Log.d("TEXTSTATE", "textState: $textState")
            },
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .onFocusChanged { state -> onFocusChange(state.isFocused) },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = TextUnit.Unspecified,
                letterSpacing = TextUnit.Unspecified
            ),
            cursorBrush = SolidColor(Color.White),
            decorationBox = @Composable { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        innerTextField()
                    }
                }
            }
        )

        // Indicator line
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ) {
            HorizontalDivider(
                thickness = 2.dp,

                color = if (focusState.value) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
                },

                modifier = Modifier
                    .padding(
                        start = 4.dp,
                        end = if (focusState.value) 0.dp else 64.dp,
                        bottom = 6.dp
                    )
            )
        }
    }
}

@Composable
fun SortButton(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val sortOptions = listOf(SortOption.NAME, SortOption.RECENT, SortOption.CREATED)
    var selectedOption by remember { mutableStateOf(viewModel.sortedOption) }
    println("From HomeList: Current selected option is '$selectedOption'")

    Box(
        modifier = modifier
            .width(144.dp)
            .width(IntrinsicSize.Max)
    ) {
        // Sort button
        Button(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                //.border(1.dp, Color.Red)
                .align(Alignment.BottomCenter)
                .padding(top = 4.dp)
        ) {
            Box () {
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Sort: $selectedOption",
                    overflow = TextOverflow.Visible,
                    maxLines = 1,
                    modifier = Modifier
                        .wrapContentWidth()
                    //.border(1.dp, Color.Green)
                )
                // DropdownMenu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 1f))
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.sortLists(option)
                                selectedOption = viewModel.sortedOption
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = option.toString(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            },
                            modifier = Modifier
                                .background(Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListCard(list: CheckList, search: String, viewModel: HomeViewModel) {

    val focusManager = LocalFocusManager.current
    val todos = viewModel.getTodosByListId(list.id)

    return Card(
        onClick = {
            viewModel.clickList(list)
            focusManager.clearFocus()
        },
        modifier = if (todos.isEmpty()) {
            Modifier.aspectRatio(1f)
        } else {
            Modifier
        }
    ) {
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (list.title != null) {
                    Text(
                        list.title,
                        style = TextStyle(fontSize = 20.sp),
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(5f),
                    )
                } else {
                    ListTextField(list, viewModel)
                }

                DropdownSettingsMenu()

            }
            for (todo in todos) {
                if (!todo.title.isNullOrEmpty()) {
                    Text(
                        getTodoTitleWithHighlight(todo.title, search),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }

}

private fun getTodoTitleWithHighlight(todoTitle: String, search: String): AnnotatedString {
    return buildAnnotatedString {
        var searchStringIndex = 0
        todoTitle.forEachIndexed { index, char ->
            if (char.lowercaseChar() == search[searchStringIndex].lowercaseChar()) {
                searchStringIndex++
                if (searchStringIndex == search.length) {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Blue,
                        )
                    ) {
                        val start = index - (searchStringIndex - 1)
                        val end = index + searchStringIndex - (searchStringIndex - 1)
                        if (start < 0 || todoTitle.length < start || end < 0 || todoTitle.length < end || end <= start) {
                            Log.d("HIGHLIGHTING", "start: $start, end: $end, search.length: ${search.length}, todoTitle: $todoTitle")
                            return AnnotatedString("Error")
                        }
                        append(
                            todoTitle.substring(start, end)
                        )
                    }
                    searchStringIndex = 0
                }
            } else {
                append(char)
            }
        }
    }
}

@Composable
private fun ListTextField(list: CheckList, viewModel: HomeViewModel){
    val focusRequester = remember { FocusRequester() }
    var isEnabled by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    val blankTitle = "Unnamed list"
    var title by remember { mutableStateOf("") }

    Box(){
        LaunchedEffect(Unit) {
            isEnabled = true
            isFocused = false
            focusRequester.requestFocus()
        }

        DisposableEffect(Unit) {
            onDispose {
                if (title.isBlank()) {
                    title = blankTitle
                }
                viewModel.updateList(
                    list.copy(title = title)
                )
            }
        }


        BasicTextField(
            value = title,
            onValueChange = { newTitle ->
                title = newTitle
            },
            singleLine = true,
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .focusRequester(focusRequester)
                // Handle title update to Room SQL when unfocused
                .onFocusChanged {
                    isFocused = !isFocused
                    if (!isFocused) {
                        if (title.isBlank()) {
                            title = blankTitle
                        }
                        viewModel.updateList(
                            list.copy(title = title)
                        )
                        isEnabled = false
                    }
                },
            enabled = isEnabled,
        )

        // Hint text when title is blank
        if (title.isBlank()) {
            Text(
                text = "Enter new title",
                color = Color.Gray,
                fontSize = 20.sp,
            )
        }
    }
}