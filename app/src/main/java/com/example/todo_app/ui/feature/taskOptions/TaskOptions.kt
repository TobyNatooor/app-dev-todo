package com.example.todo_app.ui.feature.taskOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.todo_app.data.AppDatabase
import com.example.todo_app.model.ToDo

@Composable
fun TaskOptions(
    task: ToDo,
    viewmodel: TaskOptionsViewModel,
    db: AppDatabase,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {
        // Title
        Text(
            text = "Edit Task",
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 54.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 75.dp, bottom = 75.dp)
        )

        // Options
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxSize()
        ) {
            // Task title
            item {
                Option(
                    optionTitle = "Task title",
                    content = {
                        TextFieldOption(
                            startText = task.title.toString(),
                            hintText = "Enter task title",
                            height = 42.dp,
                            contentAlign = Alignment.Center,
                            onTextChanged = { title -> viewmodel.updateTask(task.copy(title = title)) }
                        )
                    }
                )
            }


            // Move to list
            item {
                Option(
                    optionTitle = "Move to list",
                    content = {
                        TextFieldOption(
                            startText = "-- choose list --",
                            hintText = "-- choose list --",
                            height = 42.dp,
                            contentAlign = Alignment.Center,
                            onTextChanged = {  }
                        )
                    }
                )
            }


            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                                onTextChanged = {  }
                            )
                        }
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
                                onTextChanged = {  }
                            )
                        }
                    )
                }
            }


            // Task description
            item {
                Option(
                    optionTitle = "Description",
                    content = {
                        TextFieldOption(
                            startText = task.description,
                            hintText = "Enter task description",
                            height = 192.dp,
                            contentAlign = Alignment.TopStart,
                            onTextChanged = { description -> viewmodel.updateTask(task.copy(description = description)) }
                        )
                    }
                )
            }

        }
    }
}

@Composable
private fun Option(
    optionTitle: String,
    content: @Composable () -> Unit
) {
    Column (verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = optionTitle,
            color = Color.White,
            fontSize = 16.sp,
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
            .border(1.dp, Color.White, shape = shape)
            .background(
                color = Color(0xFF6A6E90),
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
            cursorBrush = SolidColor(Color.White),
            textStyle = TextStyle(
                fontSize = 18.sp,
                color = Color.White,
                textAlign = textAlign
            ),
            decorationBox = @Composable { innerTextField ->
                Box(
                    contentAlignment = contentAlign,
                    modifier = modifier
                        .padding(8.dp)
                ) {
                    if (textState.value.isEmpty() && !focusState.value) {
                        Text(
                            text = hintText,
                            fontSize = 18.sp,
                            color = Color.Gray,
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
