package com.example.todo_app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.todo_app.model.SortOption
import com.example.todo_app.ui.feature.common.BackButton
//import com.example.todo_app.ui.feature.common.SearchButton
import com.example.todo_app.ui.feature.common.SortButton

@Composable
fun AppBar(
    actions: List<AppBarAction>,
    onBackClicked: (() -> Unit)? = null,
    onSortClicked: ((SortOption) -> Unit)? = null,
    onSearchClicked: ((String) -> Unit)? = null,
    buttonColor: Color
) {
    return Row(
        modifier = Modifier
            .height(56.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        actions.forEach { action ->
            when (action) {
                is AppBarAction.Back -> {
                    BackButton(
                        onBackClicked = onBackClicked,
                        buttonColor = buttonColor
                    )
                }

                is AppBarAction.Sort -> {
                    Spacer(modifier = Modifier.weight(1f))
                    SortButton(
                        onSortClicked = onSortClicked,
                        buttonColor = buttonColor
                    )
                }

                is AppBarAction.Search -> { null }
                    /*SearchButton(
                        onSearchClicked = onSearchClicked
                    )
                }*/
            }
        }
    }
}