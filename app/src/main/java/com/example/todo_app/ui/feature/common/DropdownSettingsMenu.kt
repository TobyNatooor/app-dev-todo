package com.example.todo_app.ui.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.todo_app.ui.theme.*

@Composable
fun DropdownSettingsMenu(
        onRenameClicked: () -> Unit,
        onDeleteClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val menuTextStyle = TextStyle(
        color = neutral4,
        fontSize = 16.sp,
        textAlign = TextAlign.Center
    )

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "Settings", tint = neutral0)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(neutral1)
        ) {
            /*DropdownMenuItem(
                text = {
                    Text(
                        "Share",
                        style = menuTextStyle
                    )
                },
                onClick = { expanded = false
                            /* Handle Share */ }
            )*/
            /*DropdownMenuItem(
                text = {
                    Text(
                        "Edit",
                        style = menuTextStyle
                    )
                },
                onClick = { expanded = false
                            /* Handle Edit */ }
            )*/
            DropdownMenuItem(
                text = {
                    Text(
                        "Rename",
                        style = menuTextStyle
                    )
                },
                onClick = { expanded = false
                            onRenameClicked() }
            )
            /*DropdownMenuItem(
                text = {
                    Text(
                        "Merge",
                        style = menuTextStyle
                    )
                },
                onClick = { expanded = false
                            /* Handle Merge */ }
            )*/
            DropdownMenuItem(
                text = {
                    Text(
                        "Delete",
                        style = menuTextStyle
                    )
                },
                onClick = { expanded = false
                            onDeleteClicked() }
            )
        }
    }
}