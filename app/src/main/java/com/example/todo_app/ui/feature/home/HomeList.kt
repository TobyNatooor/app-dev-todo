package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.ImeAction
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
import com.example.todo_app.ui.feature.common.DeleteDialog
import com.example.todo_app.ui.feature.common.DropdownSettingsMenu
import com.example.todo_app.ui.feature.common.DropdownSettingsMenuItem
import com.example.todo_app.ui.feature.common.NameList
import com.example.todo_app.ui.theme.dosisFontFamily
import com.example.todo_app.ui.theme.neutral0
import com.example.todo_app.ui.theme.neutral1
import com.example.todo_app.ui.theme.neutral2
import com.example.todo_app.ui.theme.primary0
import com.example.todo_app.ui.theme.primary1
import com.example.todo_app.ui.theme.primary2
import com.example.todo_app.ui.theme.yellow2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeList(
    favorites: List<CheckList>,
    lists: List<CheckList>,
    viewModel: HomeViewModel,
    columnState: LazyListState,
    appBar: @Composable () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val newListState = viewModel.newListState.collectAsState().value
    val searchQuery by viewModel.filteringQuery.collectAsState()
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
            if(favorites.isNotEmpty()){
//                item {
//                    Box(modifier = Modifier) {
//                        Icon(Icons.Rounded.MoreVert, contentDescription = "Settings", tint = neutral0)
//                    }
//                }
                item {
                    Box(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                        val cards = buildList {
                            favorites.forEach { checklist ->
                                add(GridCard.CheckListType
                                    .CheckListGridCard(viewModel, checklist, searchQuery)
                                )
                            }
                        }
                        FavoriteCheckListGrid(cards = cards, cardSpacing = horizontalPadding)
                    }
                }
            }
            stickyHeader {
                appBar()
            }

            /*item {
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
            }*/

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
                            if (viewModel.getQuery().isEmpty()) {
                                add(GridCard
                                    .SmartListGridCard(viewModel)
                                )
                            }

                            if (newListState is NewListState.Data) {
                                add(GridCard.CheckListType
                                    .NewCheckListGridCard(viewModel, newListState.list)
                                )
                            }

                            listsWithoutNewList.forEach { checklist ->
                                add(GridCard.CheckListType
                                    .CheckListGridCard(viewModel, checklist, searchQuery)
                                )
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
                            val currentChar = if(card.title == "smart list") '\u0000'
                            else card.title.firstOrNull()?.uppercaseChar() ?: '\u0000'

                            val isNext = if(card.title == "smart list") false
                            else viewModel.isNextChar(currentChar, previousChar)

                            AlphabeticalHeader(
                                prevChar = previousChar,
                                currChar = currentChar,
                                isNext = isNext
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
private fun FavoriteCheckListGrid(
    cards: List<GridCard>,
    cardSpacing: Dp
) {
    Column(verticalArrangement = Arrangement.spacedBy(cardSpacing)) {
        Box(modifier = Modifier) {
            Icon(Icons.Rounded.Star, contentDescription = "Favorite", tint = yellow2)
        }
        cards.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(cardSpacing)) {
                rowItems.forEach { card ->
                    Column(modifier = Modifier.weight(1f)) {
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

abstract class GridCard(open val viewModel: HomeViewModel) {
    abstract var title: String

    private val textStyle = TextStyle(
        fontSize = 24.sp,
        fontFamily = dosisFontFamily,
        color = neutral0,
        textAlign = TextAlign.Center
    )

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
                    DeleteDialog(
                        id = list.id,
                        title = "Delete list \"$title\"?",
                        text = "Are you sure you want to delete this list?",
                        onDelete = { viewModel.deleteList(list.id) },
                        onDismiss = { showDeleteDialog = false },
                    )
                }
            }
            is CheckListType.NewCheckListGridCard -> isNaming = true
            is SmartListGridCard -> modifier = Modifier.aspectRatio(1f)
        }

        Card(
            onClick = {
                if (!isNaming) {
                    when (this) {
                        is SmartListGridCard -> viewModel.clickedSmartList()
                        is CheckListType -> viewModel.clickList(list)
                    }
                    focusManager.clearFocus()
                }
            },
            colors = CardDefaults.cardColors(containerColor = neutral2),
            modifier = modifier
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        if (!isNaming) {
                            Text(
                                title,
                                style = textStyle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                color = neutral0
                            )
                        } else if (this@GridCard is CheckListType) {
                            NameList(
                                title = title,
                                textStyle = textStyle,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f),
                                selectAllText = true,
                                onTitleChange = { newTitle ->
                                    title = newTitle
                                    if (this@GridCard is CheckListType.CheckListGridCard) {
                                        viewModel.updateList(list.copy(title = newTitle))
                                    }
                                },
                                onRenameComplete = {
                                    isNaming = false
                                    if (this@GridCard is CheckListType.NewCheckListGridCard) {
                                        viewModel.addNewList(title)
                                    }
                                }
                            )
                        }
                    }

                    if (this@GridCard is CheckListType.CheckListGridCard) {
                        todos.filter { it.title.isNotEmpty() }.forEach { todo ->
                            val search = viewModel.getQuery()
                            Text(
                                if (search.isNotEmpty()) {
                                    getTodoTitleWithHighlight(todo.title, searchQuery)
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

                when (this@GridCard) {
                    is CheckListType.CheckListGridCard -> {
                        if (!isNaming) {
                            DropdownSettingsMenu(
                                isFavorite = list.favorite,
                                actions = listOf(
                                    DropdownSettingsMenuItem.Rename,
                                    DropdownSettingsMenuItem.Delete,
                                    DropdownSettingsMenuItem.Favorite
                                ),
                                onRenameClicked = { isNaming = true },
                                onDeleteClicked = { showDeleteDialog = true },
                                onFavoriteClicked = { viewModel.updateList(list.copy(favorite = !list.favorite)) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(width = 38.dp, height = 44.dp)
                                    .offset(x = 8.dp, y = (-2).dp)
                            )
                        }
                    }
                    is SmartListGridCard -> {
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
                    is CheckListType.NewCheckListGridCard -> {}
                }
            }
        }
    }

    // Definition of GridCard types
    abstract class CheckListType(
        viewModel: HomeViewModel,
        open val list: CheckList,
        override var title: String = list.title
    ) : GridCard(viewModel) {

        data class NewCheckListGridCard(
            override val viewModel: HomeViewModel,
            override val list: CheckList
        ) : CheckListType(viewModel, list)

        data class CheckListGridCard(
            override val viewModel: HomeViewModel,
            override val list: CheckList,
            val searchQuery: String
        ) : CheckListType(viewModel, list) {
            val todos: List<ToDo> = viewModel.getTodosByListId(list.id)
        }
    }

    data class SmartListGridCard(
        override val viewModel: HomeViewModel
    ) : GridCard(viewModel) {
        override var title = "Smart List"
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
                    withStyle(style = highlightStyle) { append(todoTitle.substring(i-j+1, i+1)) }
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
