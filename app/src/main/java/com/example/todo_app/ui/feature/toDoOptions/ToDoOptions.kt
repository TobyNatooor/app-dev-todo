package com.example.todo_app.ui.feature.toDoOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.CustomDropdownMenu
import com.example.todo_app.ui.theme.*

@Composable
fun ToDoOptions(
    toDo: ToDo,
    checklists: List<CheckList>,
    viewmodel: ToDoOptionsViewModel,
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {
        // Title
        Text(
            color = neutral1,
            fontFamily = dosisFontFamily,
            text = "Edit Task",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 54.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 100.dp, bottom = 100.dp)
        )
        Box(
            modifier = Modifier.padding(32.dp)
        ) {
            appBar()
        }
        // Options
        Card(
            colors = CardDefaults.cardColors(
                containerColor = neutral2,
            ),
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxSize()
        ) {
            // To-Do title
            item {
                Option(
                    optionTitle = "Task title",
                    content = {
                        TextFieldOption(
                            startText = toDo.title.toString(),
                            hintText = "Enter task title",
                            height = 42.dp,
                            contentAlign = Alignment.Center,
                            onTextChanged = { title -> viewmodel.updateToDo(toDo.copy(title = title)) }
                        )
                    }
                )
            }
            // Move to list
            item {
                Option(
                    optionTitle = "Move to list",
                    content = {
                        DropdownMenuOption(
                            hintText = checklists.find { it.id == toDo.listId }?.title
                                ?: "Select a list",
                            height = 42.dp,
                            contentAlign = Alignment.Center,
                            sortOptions = checklists.filter { it.id != toDo.listId }
                                .map { DropdownOptionItem(it.id, it.title.toString()) },
                            onOptionSelected = { selectedOption ->
                                val updatedToDo = toDo.copy(listId = selectedOption.id)
                                viewmodel.updateToDo(updatedToDo)
                            }
                        )
                    }
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // Deadline
                    Option(
                        optionTitle = "Deadline",
                        content = {
                            TextFieldOption(
                                startText = "01/10/25",
                                hintText = "Select a date",
                                height = 42.dp,
                                contentAlign = Alignment.TopStart,
                                onTextChanged = { }
                            )
                        },
                        modifier = Modifier.weight(0.5f)
                    )
                    // Time estimate
                    Option(
                        optionTitle = "Time estimate",
                        content = {
                            TextFieldOption(
                                startText = "00:00",
                                hintText = "00:00",
                                height = 42.dp,
                                contentAlign = Alignment.TopStart,
                                onTextChanged = { }
                            )
                        },
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }
            // To-Do description
            item {
                Option(
                    optionTitle = "Description",
                    content = {
                        TextFieldOption(
                            startText = toDo.description,
                            hintText = "Enter task description",
                            height = 150.dp,
                            contentAlign = Alignment.TopStart,
                            onTextChanged = { description ->
                                viewmodel.updateToDo(
                                    toDo.copy(
                                        description = description
                                    )
                                )
                            }
                        )
                    }
                )
            }

        }
        }
    }
}

@Composable
private fun Option(
    optionTitle: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Text(
            text = optionTitle,
            color = neutral0,
            fontSize = 16.sp,
            fontFamily = dosisFontFamily,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
        content()
    }
}

@Composable
private fun TextFieldOption(
    startText: String,
    hintText: String,
    height: Dp,
    onTextChanged: (String) -> Unit,
    contentAlign: Alignment
) {
    val focusRequester = remember { FocusRequester() }
    val textState = remember { mutableStateOf(startText) }
    val focusState = remember { mutableStateOf(false) }
    val onFocusChange: (Boolean) -> Unit = { isFocused ->
        focusState.value = isFocused
    }
    val modifier = Modifier.fillMaxWidth()
    val shape = RoundedCornerShape(12.dp)
    val textAlign = when (contentAlign) {
        Alignment.Center -> TextAlign.Center
        Alignment.TopStart -> TextAlign.Left
        else -> TextAlign.Left
    }

    Box(
        modifier = modifier
            .height(height)
            .background(
                color = primary0,
                shape = shape
            ),
        contentAlignment = contentAlign
    ) {
        BasicTextField(
            value = textState.value,
            onValueChange = { newText ->
                textState.value = newText
                onTextChanged(newText)
            },
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { state -> onFocusChange(state.isFocused) },
            cursorBrush = SolidColor(primary4),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontFamily = dosisFontFamily,
                color = primary4,
                textAlign = textAlign
            ),
            decorationBox = @Composable { innerTextField ->
                Box(
                    contentAlignment = contentAlign,
                    modifier = modifier
                        .padding(10.dp)
                ) {
                    if (textState.value.isEmpty() && !focusState.value) {
                        Text(
                            text = hintText,
                            fontSize = 18.sp,
                            fontFamily = dosisFontFamily,
                            color = primary1,
                            textAlign = textAlign,
                            modifier = modifier
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
private fun DropdownMenuOption(
    hintText: String,
    height: Dp,
    sortOptions: List<DropdownOptionItem>,
    onOptionSelected: (DropdownOptionItem) -> Unit,
    contentAlign: Alignment = Alignment.Center
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption: DropdownOptionItem? by remember { mutableStateOf(null) }
    val shape = RoundedCornerShape(12.dp)
    val textAlign = when (contentAlign) {
        Alignment.Center -> TextAlign.Center
        Alignment.TopStart -> TextAlign.Left
        else -> TextAlign.Center
    }
    val modifier = Modifier.fillMaxWidth()

    Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
        Box(
            modifier = modifier
                .height(height)
                .background(color = primary0, shape = shape)
                .clickable { expanded = !expanded },
            contentAlignment = contentAlign
        ) {
            Text(
                text = selectedOption?.title ?: hintText,
                fontSize = 18.sp,
                fontFamily = dosisFontFamily,
                color = primary4,
                textAlign = textAlign,
                modifier = Modifier.padding(10.dp)
            )
        }

        CustomDropdownMenu(
            modifier = modifier
                .background(color = primary0, shape = shape)
                .padding(vertical = 4.dp),
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = primary4,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            contentAlign = contentAlign,
            expanded = expanded,
            items = sortOptions,
            onDismiss = { expanded = false },
            onItemSelected = {
                selectedOption = it
                onOptionSelected(it)
            },
            itemContent = { item, index ->
                Box(
                    modifier = modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    contentAlignment = contentAlign
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontFamily = dosisFontFamily,
                        color = primary4,
                        textAlign = textAlign
                    )
                }
            }
        )
    }
}

data class DropdownOptionItem(
    val id: Int,
    val title: String
)
