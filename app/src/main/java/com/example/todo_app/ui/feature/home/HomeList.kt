package com.example.todo_app.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.ui.feature.common.*

@Composable
fun HomeList(
    lists: List<CheckList>,
    viewModel: HomeViewModel,
    gridState: LazyGridState
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
                fontSize = 60.sp,
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
                        start = horizontalPadding / 2,
                        end = horizontalPadding / 4
                    ),
                verticalAlignment = Alignment.Bottom
            ) {
                val childrenHeight = 42.dp
                val horizontalDistribution = 8f/15f

                SearchTextField(
                    viewModel,
                    focusRequester,
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
                            text = "No checklists yet",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    items(lists.size) { index ->
                        ListCard(lists[index], viewModel)
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
    modifier: Modifier = Modifier
) {
    val textState = remember { mutableStateOf("") }
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
            onValueChange = { textState.value = it },
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
    val sortOptions = listOf("Name", "Recent", "Date")
    var selectedOption by remember { mutableStateOf(sortOptions[0]) }

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
                                selectedOption = option
                                expanded = false
                            },
                            text = {
                                Text(
                                    text = option,
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
private fun ListCard(list: CheckList, viewModel: HomeViewModel) {
    val focusManager = LocalFocusManager.current
    var isNaming by remember { mutableStateOf(false) }

    return Card(
        onClick = {
            if (!isNaming) {
                viewModel.clickList(listTitle = list.title.toString(), listId = list.id)
            }
            focusManager.clearFocus()
        },
        modifier = Modifier.aspectRatio(1f)
    ) {
        Column(modifier = Modifier.padding(10.dp, 10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isNaming || list.title == null) {
                    NameList(
                        title = list.title,
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
                        style = TextStyle(fontSize = 20.sp),
                        textAlign = TextAlign.Justify,
                        modifier = Modifier.weight(5f),
                    )
                }

                DropdownSettingsMenu(
                    onRenameClicked = { isNaming = true }
                )

            }
        }
    }
}
