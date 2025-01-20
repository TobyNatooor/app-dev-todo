package com.example.todo_app.ui.feature.home

import android.util.Log
import androidx.collection.intListOf
import androidx.collection.mutableIntListOf
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.example.todo_app.ui.feature.common.DeleteList
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu
import com.example.todo_app.ui.feature.common.DropdownSettingsMenuItem
import com.example.todo_app.ui.feature.common.NameList
import com.example.todo_app.ui.theme.dosisFontFamily
import com.example.todo_app.ui.theme.*

@Composable
fun HomeList(
    lists: List<CheckList>,
    viewModel: HomeViewModel,
    columnState: LazyListState
) {
    val focusManager = LocalFocusManager.current

    val horizontalPadding = 24.dp
    val addingNewList = viewModel.addingNewList.collectAsState()

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
        LazyColumn(
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
                    fontFamily = dosisFontFamily,
                    color = neutral1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 120.dp, bottom = 100.dp)
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
                    if (lists.isEmpty() && !addingNewList.value) {
                        Text(
                            text = "No checklists found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosisFontFamily
                        )
                    } else {
                        val cards = buildList {
                            if (addingNewList.value) add(ChecklistCardItem("") {
                                NewListCard(viewModel)
                            })
                            add(ChecklistCardItem(
                                "smart list"
                            ) {
                                SmartList(viewModel)
                            })
                            lists.forEach { checklist ->
                                add(ChecklistCardItem(checklist.title) {
                                    ListCard(checklist, viewModel)
                                })
                            }
                        }

                        CheckListGrid(
                            viewModel = viewModel,
                            cards = cards,
                            cardSpacing = horizontalPadding
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckListGrid(
    viewModel: HomeViewModel,
    cards: List<ChecklistCardItem>,
    cardSpacing: Dp
) {
    val sortedOption = viewModel.sortedOption.collectAsState()
    var previousChar = '\u0000'

    Column(verticalArrangement = Arrangement.spacedBy(cardSpacing)) {
        cards.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(cardSpacing)) {
                rowItems.forEach { card ->
                    Column(modifier = Modifier.weight(1f)) {
                        if (sortedOption.value == SortOption.NAME) {
                            val currentChar = card.title.firstOrNull()?.uppercaseChar() ?: '\u0000'

                            AlphabeticalHeader(
                                prevChar = previousChar,
                                currChar = currentChar,
                                isNext = viewModel.isNextChar(currentChar, previousChar)
                            ) { viewModel.getSymbol(currentChar) }

                            if (previousChar != currentChar) {
                                previousChar = currentChar
                            }
                        }

                        card.item()
                    }
                }
                if (rowItems.size == 1) Box(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AlphabeticalHeader(
    prevChar: Char,
    currChar: Char,
    isNext: Boolean,
    getSymbol: (Char) -> String
) {
    if (isNext) {
        println("Creating header with $currChar")
        Text(
            getSymbol(currChar),
            style = TextStyle(fontSize = 13.sp, fontFamily = dosisFontFamily),
            color = neutral1,
        )
        HorizontalDivider(
            modifier = Modifier
                .width(15.dp)
                .height(4.dp)
        )
    } else {
        // Space instead of text
        Spacer(modifier = Modifier.height(20.dp))
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
private fun SearchTextField(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val focusState = remember { mutableStateOf(false) }
    val searchQuery = viewModel.filteringQuery.collectAsState()
    val userInput = remember { mutableStateOf(searchQuery.value) }

    val onFocusChange: (Boolean) -> Unit = { isFocused ->
        focusState.value = isFocused
    }

    Box(modifier = modifier) {
        // Search TextField
        BasicTextField(
            value = userInput.value,
            onValueChange = { newTitle ->
                userInput.value = newTitle
                viewModel.searchForTodos(userInput.value)
            },
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { state -> onFocusChange(state.isFocused) },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = neutral0,
                fontFamily = dosisFontFamily,
                lineHeight = TextUnit.Unspecified,
                letterSpacing = TextUnit.Unspecified
            ),
            cursorBrush = SolidColor(neutral0),
            decorationBox = @Composable { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = neutral1
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
                    neutral1
                } else {
                    Color.Transparent
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
    val selectedOption = viewModel.sortedOption.collectAsState()

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
                contentColor = neutral1
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 4.dp)
        ) {
            Box {
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    fontFamily = dosisFontFamily,
                    text = "Sort: ${selectedOption.value}",
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
                        .background(neutral1)
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                viewModel.sortLists(option)
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = option.toString(),
                                    color = neutral4,
                                    fontSize = 16.sp,
                                    fontFamily = dosisFontFamily,
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
    viewModel: HomeViewModel
) {
    val focusManager = LocalFocusManager.current
    var isNaming by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val todos = viewModel.getTodosByListId(list.id)
    val search = viewModel.getQuery()

    if (showDeleteDialog) {
        DeleteList(
            listId = list.id,
            title = list.title,
            onDelete = { viewModel.deleteList(list.id) },
            onDismiss = { showDeleteDialog = false }
        )
    }

    return Card(
        onClick = {
            if (!isNaming) {
                viewModel.clickList(list)
            }
            focusManager.clearFocus()
        },
        colors = CardDefaults.cardColors(
            containerColor = neutral2,
        ),
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
                if (isNaming) {
                    NameList(
                        title = list.title,
                        textStyle = null,
                        modifier = null,
                        onTitleChange = { newTitle ->
                            viewModel.updateList(list.copy(title = newTitle))
                        },
                        onRenameComplete = {
                            isNaming = false // Reset naming state
                        }
                    )
                } else {
                    Text(
                        list.title,
                        style = TextStyle(fontSize = 20.sp, fontFamily = dosisFontFamily),
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(5f),
                        color = neutral0
                    )

                    DropdownSettingsMenu(
                        actions = listOf(
                            DropdownSettingsMenuItem.Rename,
                            DropdownSettingsMenuItem.Delete
                        ),
                        onRenameClicked = { isNaming = true },
                        onDeleteClicked = { showDeleteDialog = true }
                    )
                }
            }
            for (todo in todos) {
                if (todo.title.isNotEmpty()) {
                    Text(
                        getTodoTitleWithHighlight(todo.title, search),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = dosisFontFamily
                    )
                }
            }
        }
    }
}

@Composable
private fun NewListCard(
    viewModel: HomeViewModel
) {
    val focusRequester = remember { FocusRequester() }
    return Card(
        colors = CardDefaults.cardColors(
            containerColor = neutral2,
        ),
        modifier = Modifier.aspectRatio(1f)
    ) {
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                NewListTextField(focusRequester, viewModel)
            }
        }
    }

}

private fun getTodoTitleWithHighlight(todoTitle: String, search: String): AnnotatedString {
    val highlightStyle = SpanStyle(
        color = primary0,
        background = primary2,
        fontFamily = dosisFontFamily
    )
    val normalStyle = SpanStyle(
        color = primary0,
        fontFamily = dosisFontFamily
    )

    if (search.isEmpty()) {
        return AnnotatedString(todoTitle, normalStyle)
    }

    var i = 0
    var j = 0
    return buildAnnotatedString {
        while (i < todoTitle.length) {
            if (search[j].lowercaseChar() == todoTitle[i].lowercaseChar()) {
                j++
                if (j == search.length) {
                    withStyle(style = highlightStyle) { append(search) } // TODO: take into account lowercase letters
                    j = 0
                }
            } else {
                while (j > 0) {
                    withStyle(style = normalStyle) { append(todoTitle[i - j]) }
                    j--
                }
                withStyle(style = normalStyle) { append(todoTitle[i]) }
            }
            i++
        }
    }
}

@Composable
private fun NewListTextField(
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
                //viewModel.addList(title)
            }
        }
        BasicTextField(
            value = title,
            onValueChange = { newTitle ->
                title = newTitle
            },
            singleLine = true,
            textStyle = TextStyle(
                color = neutral0,
                fontSize = 20.sp,
                fontFamily = dosisFontFamily
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
                        viewModel.addList(title)
                        isEnabled = false
                    }
                },
            enabled = isEnabled,
        )

        // Hint text when title is blank
        if (title.isBlank()) {
            Text(
                text = "Enter new title",
                color = neutral1,
                fontSize = 20.sp,
                fontFamily = dosisFontFamily
            )
        }
    }
}

@Composable
fun SmartList(
    viewModel: HomeViewModel
) {
    return Card(
        onClick = {
            viewModel.clickedSmartList()
        },
        colors = CardDefaults.cardColors(
            containerColor = neutral2,
        ),
        modifier = Modifier.aspectRatio(1f)
    ) {
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Smart List",
                    style = TextStyle(fontSize = 20.sp, fontFamily = dosisFontFamily),
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.weight(5f),
                    color = neutral0
                )
                Icon(
                    Icons.Filled.AutoAwesome,
                    contentDescription = "Smart List Icon",
                    tint = primary1,
                    modifier = Modifier.size(32.dp)
                )

            }
        }
    }
}

data class ChecklistCardItem(
    val title: String,
    val item: @Composable () -> Unit
)
