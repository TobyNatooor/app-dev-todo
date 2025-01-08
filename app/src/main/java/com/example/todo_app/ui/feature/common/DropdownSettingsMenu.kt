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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun DropdownSettingsMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box() {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "Settings")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background.copy(alpha = 1f))
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        "Share",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                    /* Handle Share */ }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Edit",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                    /* Handle Edit */ }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Rename",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                    /* Handle Rename */ }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Merge",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                    /* Handle Merge */ }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                onClick = { expanded = false
                    /* Handle Delete */ }
            )
        }
    }
}