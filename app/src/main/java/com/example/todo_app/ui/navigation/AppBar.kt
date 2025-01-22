package com.example.todo_app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Arrangement
import androidx.room.Query
import com.example.todo_app.model.SortOption
import com.example.todo_app.ui.feature.common.SearchButton
import com.example.todo_app.ui.feature.common.SortButton
import com.example.todo_app.ui.theme.*

@Composable
fun AppBar(
    actions: List<AppBarAction>,
    onBackClicked: (() -> Unit)? = null,
    onSortClicked: ((SortOption) -> Unit)? = null,
    onSearchClicked: ((String) -> Unit)? = null,
    getQuery: (() -> String)? = null,
    sortOptions: List<SortOption>
) {
    return Row(
        modifier = Modifier
            .height(56.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions.filterIsInstance<AppBarAction.Back>().forEach { action ->
                Icon(
                    Icons.Filled.ChevronLeft,
                    contentDescription = "Back",
                    tint = neutral1,
                    modifier = Modifier
                        .fillMaxHeight()
                        .size(32.dp)
                        .clickable {
                            onBackClicked?.invoke()
                        }
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions.filter { it !is AppBarAction.Back }.forEach { action ->
                when (action) {
                    is AppBarAction.Sort -> {
                        SortButton(
                            onSortClicked = onSortClicked,
                            sortOptions
                        )
                    }
                    is AppBarAction.Search -> {
                        SearchButton(
                            onSearchClicked = onSearchClicked,
                            getQuery = { query(getQuery) }
                        )
                    }
                    is AppBarAction.Back -> {
                        // Do nothing
                    }
                }
            }
        }
    }
}

private fun query(
    getQuery: (() -> String)? = null,
    ): String {
    if(getQuery != null) {
        return getQuery()
    }else {
        return "How did you mess this up?"
    }
}
private fun youDoneFuckedUp(): String {
    return "How did you mess this up?"
}