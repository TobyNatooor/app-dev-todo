package com.example.todo_app.ui.feature.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.ui.theme.*

@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(65.dp)
            .border(1.dp, neutral4, RoundedCornerShape(25, 25, 25, 25))
    ) {
        FloatingActionButton(
            onClick = onClick,
            // Remove shape parameter for default shape (square with rounded corners)
            // shape = RoundedCornerShape(45, 45, 45, 45),
            containerColor = primary2,
            contentColor = primary1,
            modifier = Modifier
                .padding(0.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                modifier = Modifier
                    .size(48.dp),
                contentDescription = "Add new item",
                tint = primary4,
            )
        }
    }
}

@Composable
fun NameList(
    title: String?,
    textStyle: TextStyle?,
    modifier: Modifier?,
    onTitleChange: (String) -> Unit,
    onRenameComplete: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var isEnabled by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    val blankTitle = "Unnamed list"
    var textFieldValue by remember { mutableStateOf(TextFieldValue(title ?: "", TextRange((title ?: "").length))) }

    Box {
        LaunchedEffect(Unit) {
            isEnabled = true
            isFocused = false
            focusRequester.requestFocus()
        }

        DisposableEffect(Unit) {
            onDispose {
                if (textFieldValue.text.isBlank()) {
                    textFieldValue = TextFieldValue(blankTitle)
                }
                onTitleChange(textFieldValue.text)
                onRenameComplete()
            }
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue -> textFieldValue = newValue },
            textStyle = textStyle ?: TextStyle(fontSize = 20.sp, color = neutral0, fontFamily = dosisFontFamily),
            cursorBrush = SolidColor(neutral0),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .focusRequester(focusRequester)
                // Handle title update to Room SQL when unfocused
                .onFocusChanged {
                    isFocused = !isFocused
                    if (!isFocused) {
                        if (textFieldValue.text.isBlank()) {
                            textFieldValue = TextFieldValue(blankTitle)
                        }
                        onTitleChange(textFieldValue.text)
                        isEnabled = false
                        onRenameComplete()
                    }
                },
            enabled = isEnabled,
        )

        if (textFieldValue.text.isBlank()) {
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
fun DeleteList(
    listId: Int,
    title: String,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        containerColor = primary0,
        titleContentColor = primary4,
        textContentColor = primary3,
        onDismissRequest = onDismiss,
        title = { Text("Delete list \"$title\"?", fontFamily = dosisFontFamily) },
        text = { Text("Are you sure you want to delete this list?", fontFamily = dosisFontFamily) },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary3,
                    contentColor = primary0
                ),
                onClick = {
                    onDelete(listId)
                    onDismiss()
                }
            ) {
                Text("Delete", fontFamily = dosisFontFamily)
            }
        },
        dismissButton = {
            Button(
                border = BorderStroke(3.dp, primary3),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary0,
                    contentColor = primary3
                ),
                onClick = onDismiss
            ) {
                Text("Cancel", fontFamily = dosisFontFamily)
            }
        }
    )
}