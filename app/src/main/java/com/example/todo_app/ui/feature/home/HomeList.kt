package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.SortOption
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu

@Composable
fun HomeList(
    lists: List<CheckList>,
    viewModel: HomeViewModel,
    searchQuery: MutableState<String>,
    focusManager: FocusManager,
    columnState: LazyListState
) {
    val focusRequester = remember { FocusRequester() }
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
        // Lists
        LazyColumn (
            state = columnState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Title
            item {
                Text(
                    "My Lists",
                    textAlign = TextAlign.Center,
                    fontSize = 54.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, bottom = 80.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = horizontalPadding / 2,
                            end = horizontalPadding / 4,
                        ),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val childrenHeight = 42.dp
                    val horizontalDistribution = 8f / 15f

                    SearchTextField(
                        viewModel,
                        focusRequester,
                        searchQuery,
                        modifier = Modifier
                            .weight(horizontalDistribution)
                            .height(childrenHeight)
                    )

                    SortButton(
                        viewModel,
                        modifier = Modifier
                            .weight(1f - horizontalDistribution)
                            .height(childrenHeight)
                    )
                }
            }

            item {
                Box(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    if (lists.isEmpty()) {
                        Text(
                            text = "No checklists found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        CheckListGrid(viewModel, lists, searchQuery, horizontalPadding)
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckListGrid(
    viewModel: HomeViewModel,
    lists: List<CheckList>,
    searchQuery: MutableState<String>,
    horizontalPadding: Dp,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    viewModel.currentChar = '\u0000'

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(horizontalPadding)
    ) {
        lists.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { checklist ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        if (viewModel.sortedOption == SortOption.NAME) {
                            val char = viewModel.isNextChar(checklist)
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
                        ListCard(checklist, searchQuery.value, focusRequester, viewModel)
                    }
                }

                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.width(horizontalPadding))
                }
            }
        }
    }
}

@Composable
private fun SearchTextField(
    viewModel: HomeViewModel,
    focusRequester: FocusRequester,
    searchQuery: MutableState<String>,
    modifier: Modifier = Modifier
) {
    val focusState = remember { mutableStateOf(false) }

    val onFocusChange: (Boolean) -> Unit = { isFocused ->
        focusState.value = isFocused
    }

    Box(modifier = modifier) {
        // Search TextField
        BasicTextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
                viewModel.searchForTodos(it)
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
                        end = if (focusState.value) 4.dp else 64.dp,
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
    val sortOptions = listOf(SortOption.NAME, SortOption.RECENT, SortOption.CREATED)

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(viewModel.sortedOption) }

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
                .align(Alignment.BottomCenter)
                .padding(top = 4.dp)
        ) {
            Box {
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    text = "Sort: $selectedOption",
                    overflow = TextOverflow.Visible,
                    maxLines = 1,
                    modifier = Modifier
                        .wrapContentWidth()
                )
                // DropdownMenu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
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
private fun ListCard(
    list: CheckList,
    search: String,
    focusRequester: FocusRequester,
    viewModel: HomeViewModel
) {
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
                    ListTextField(list, focusRequester, viewModel)
                }

                DropdownSettingsMenu()

            }
            for (todo in todos) {
                if (!todo.title.isNullOrEmpty()) {
                    Text(
                        if (search.isNotEmpty()) {
                            getTodoTitleWithHighlight(todo.title, search)
                        } else {
                            AnnotatedString(todo.title)
                        },
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
        var searching = false
        todoTitle.forEachIndexed { index, char ->
            if (char.lowercaseChar() == search[searchStringIndex].lowercaseChar()) {
                searching = true
                searchStringIndex++
                if (searchStringIndex == search.length) {
                    searching = false
                    val start = index - (searchStringIndex - 1)
                    val end = index + searchStringIndex - (searchStringIndex - 1)
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            background = Color.Blue,
                        )
                    ) {
                        append(
                            todoTitle.substring(start, end)
                        )
                    }
                    searchStringIndex = 0
                }
            } else if (searching) {
                searching = false
                val start = index - searchStringIndex
                val end = index + searchStringIndex - (searchStringIndex - 1)
                append(todoTitle.substring(start, end))
                searchStringIndex = 0
            } else {
                append(char)
            }
        }
    }
}

@Composable
private fun ListTextField(
    list: CheckList,
    focusRequester: FocusRequester,
    viewModel: HomeViewModel
) {
    val blankTitle = "Unnamed list"
    var isEnabled by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }

    Box {
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