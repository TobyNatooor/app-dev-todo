package com.example.todo_app.ui.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.toDoList.ToDoListViewModel
import com.example.todo_app.ui.theme.*

@Composable
fun DropdownSettingsMenu(
        onRenameClicked: () -> Unit,
        onDeleteClicked: () -> Unit,
        modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val menuTextStyle = TextStyle(
        color = neutral4,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        fontFamily = dosisFontFamily
    )

    Box(modifier = modifier) {
        val focusManager = LocalFocusManager.current

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "Checklist settings",
            tint = neutral0,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    expanded = !expanded
                    focusManager.clearFocus()
                }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(neutral1)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Rename",
                        style = menuTextStyle,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                            onRenameClicked() }
            )

            DropdownMenuItem(
                text = {
                    Text(
                        "Delete",
                        style = menuTextStyle,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                            onDeleteClicked() }
            )
        }
    }
}