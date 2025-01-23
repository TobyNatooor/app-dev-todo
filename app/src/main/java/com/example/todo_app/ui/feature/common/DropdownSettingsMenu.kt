package com.example.todo_app.ui.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.ui.theme.*

@Composable
fun DropdownSettingsMenu(
    isFavorite: Boolean? = false,
    onFavoriteClicked: (() -> Unit)? = null,
    onShareClicked: (() -> Unit)? = null,
    onEditClicked: (() -> Unit)? = null,
    onRenameClicked: (() -> Unit)? = null,
    onMergeClicked: (() -> Unit)? = null,
    onDeleteClicked: (() -> Unit)? = null,
    actions: List<DropdownSettingsMenuItem>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val menuTextStyle = TextStyle(
        color = neutral4,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        fontFamily = dosisFontFamily
    )
    val favoriteText = if(isFavorite == true) "Remove from Favorites"
    else "Add to Favorites"

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.Rounded.MoreVert,
                contentDescription = "Settings",
                tint = neutral0,
                modifier = Modifier.size(32.dp)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(neutral1)
        ) {
            actions.forEach { action ->
                when (action) {
                    is DropdownSettingsMenuItem.Share -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Share",
                                    style = menuTextStyle,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                expanded = false
                                onShareClicked?.invoke()
                            }
                        )
                    }

                    is DropdownSettingsMenuItem.Edit -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Edit",
                                    style = menuTextStyle,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                expanded = false
                                onEditClicked?.invoke()
                            }
                        )
                    }

                    is DropdownSettingsMenuItem.Rename -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Rename",
                                    style = menuTextStyle,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                expanded = false
                                onRenameClicked?.invoke()
                            }
                        )
                    }
                    
                    is DropdownSettingsMenuItem.Favorite -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    favoriteText,
                                    style = menuTextStyle,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                expanded = false
                                onFavoriteClicked?.invoke()
                            }
                        )
                    }

                    is DropdownSettingsMenuItem.Merge -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Merge",
                                    style = menuTextStyle,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                expanded = false
                                onMergeClicked?.invoke()
                            }
                        )
                    }

                    is DropdownSettingsMenuItem.Delete -> {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Delete",
                                    style = menuTextStyle,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                expanded = false
                                onDeleteClicked?.invoke()
                            }
                        )
                    }
                }
            }
        }
    }
}