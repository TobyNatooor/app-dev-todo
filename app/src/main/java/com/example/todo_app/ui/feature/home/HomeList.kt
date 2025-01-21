package com.example.todo_app.ui.feature.home

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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.todo_app.model.ToDo
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
    val newListState = viewModel.newListState.collectAsState().value
    val horizontalPadding = 24.dp

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
                    if (lists.isEmpty()) {
                        Text(
                            text = "No checklists found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = dosisFontFamily
                        )
                    } else {
                        val listsWithoutNewList: MutableList<CheckList> = lists.toMutableList()

                        val cards: List<GridCard> = buildList {
                            if (newListState is NewListState.Data) {
                                add(GridCard.CheckListType.NewCheckListGridCard(viewModel, newListState.list))
                            }

                            if (viewModel.getQuery().isEmpty()) {
                                add(GridCard.SmartListGridCardType(viewModel))
                            }

                            listsWithoutNewList.forEach { checklist ->
                                add(GridCard.CheckListType.CheckListGridCard(viewModel, checklist, viewModel.getTodosByListId(checklist.id)))
                            }
                        }

                        CheckListGrid(viewModel = viewModel, cards = cards, cardSpacing = horizontalPadding)
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckListGrid(
    viewModel: HomeViewModel,
    cards: List<GridCard>,
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
private fun AlphabeticalHeader(prevChar: Char, currChar: Char, isNext: Boolean, getSymbol: (Char) -> String){
    if (isNext) {
        //println("Creating header with $currChar")
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

abstract class GridCard(open val viewModel: HomeViewModel) {
    private val textStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = dosisFontFamily,
        color = neutral0,
        textAlign = TextAlign.Center
    )

    abstract class CheckListType(
        viewModel: HomeViewModel,
        open val list: CheckList
    ) : GridCard(viewModel) {
        override val title: String
            get() = list.title

        data class NewCheckListGridCard(
            override val viewModel: HomeViewModel,
            override val list: CheckList
        ) : CheckListType(viewModel, list)

        data class CheckListGridCard(
            override val viewModel: HomeViewModel,
            override val list: CheckList,
            val todos: List<ToDo>
        ) : CheckListType(viewModel, list)
    }

    data class SmartListGridCardType(
        override val viewModel: HomeViewModel
    ) : GridCard(viewModel) {
        override val title = "Smart List"
    }

    abstract val title: String

    val item: @Composable () -> Unit = {
        val focusManager = LocalFocusManager.current

        var modifier: Modifier = Modifier
        var isNaming by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }

        when (this) {
            is CheckListType.CheckListGridCard -> {
                if (todos.isEmpty()) {
                    modifier = Modifier.aspectRatio(1f)
                }
                if (showDeleteDialog) {
                    DeleteList(
                        listId = list.id,
                        title = list.title,
                        onDelete = { viewModel.deleteList( list.id ) },
                        onDismiss = { showDeleteDialog = false }
                    )
                }
            }

            is CheckListType.NewCheckListGridCard -> {
                isNaming = true
            }

            is SmartListGridCardType -> {
                modifier = Modifier.aspectRatio(1f)
            }
        }

        Card(
            onClick = {
                when (this) {
                    is SmartListGridCardType -> viewModel.clickedSmartList()
                    is CheckListType -> viewModel.clickList(list)

                    else -> { }
                }
                if (!isNaming) focusManager.clearFocus()
            },
            colors = CardDefaults.cardColors(
                containerColor = neutral2,
            ),
            modifier = modifier
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    if (!isNaming) {
                        Text(
                            title,
                            style = textStyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            color = neutral0
                        )
                    }

                    if (this@GridCard is CheckListType.CheckListGridCard) {
                        if (isNaming) {
                            NameList(
                                title = title,
                                textStyle = textStyle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                selectAllText = true,
                                onTitleChange = { newTitle ->
                                    viewModel.updateList(list.copy(title = newTitle))
                                },
                                onRenameComplete = {
                                    isNaming = false
                                    if (viewModel.newListState.value is NewListState.Data) {
                                        viewModel.addNewList(title)
                                    }
                                }
                            )
                        }

                        for (todo in todos) {
                            if (todo.title.isNotEmpty()) {
                                val search = viewModel.getQuery()
                                Text(
                                    if (search.isNotEmpty()) {
                                        getTodoTitleWithHighlight(todo.title, search)
                                    } else {
                                        AnnotatedString(todo.title)
                                    },
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = dosisFontFamily
                                )
                            }
                        }
                    }
                }

                when(this@GridCard) {
                    is CheckListType.CheckListGridCard -> {
                        if (!isNaming) {
                            DropdownSettingsMenu(
                                actions = listOf(
                                    DropdownSettingsMenuItem.Rename,
                                    DropdownSettingsMenuItem.Delete
                                ),
                                onRenameClicked = { isNaming = true },
                                onDeleteClicked = { showDeleteDialog = true },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(width = 38.dp, height = 44.dp)
                                    .offset(x = 8.dp, y = (-2).dp)
                            )
                        }
                    }

                    is SmartListGridCardType -> {
                        Icon(
                            Icons.Filled.AutoAwesome,
                            contentDescription = "Smart List Icon",
                            tint = primary1,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(32.dp)
                                .align(Alignment.TopEnd)
                        )
                    }

                    is CheckListType.NewCheckListGridCard -> { }
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
                            color = primary0,
                            background = primary2,
                            fontFamily = dosisFontFamily
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
                withStyle(
                        style = SpanStyle(
                            color = primary0,
                            background = primary2,
                            fontFamily = dosisFontFamily
                        )
                    ) {
                        append(
                            todoTitle.substring(start, end)
                        )
                    }
                searchStringIndex = 0
            } else {
                withStyle(
                    style = SpanStyle(
                        color = neutral0,
                        fontFamily = dosisFontFamily
                    )
                ) {
                    append(char)
                }
            }
        }
    }
}
