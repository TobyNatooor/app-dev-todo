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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.*
import com.example.todo_app.ui.theme.*

@Composable
fun AppBar(
        navController: NavController,
        backButton: Boolean,
        sortButton: Boolean,
        searchButton: Boolean
) {
    return Row(
        modifier = Modifier
            .height(32.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
    ) {
        if (backButton) {
            Icon(
                Icons.Filled.ChevronLeft,
                contentDescription = null,
                tint = neutral1,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (sortButton) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = null,
                tint = neutral1,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clickable {}
            )
        }
        if (searchButton) {
            Icon(
                Icons.Filled.Search,
                contentDescription = null,
                tint = neutral1,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 8.dp) // Space between the two right-most icons
                    .aspectRatio(1f)
                    .clickable {}
            )
        }
    }
}