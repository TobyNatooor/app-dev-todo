package com.example.todo_app.ui.feature.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun <T> CustomDropdownMenu(
    expanded: Boolean,
    items: List<T>,
    onDismiss: () -> Unit,
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T, Int) -> Unit,
    modifier: Modifier = Modifier,
    contentAlign: Alignment = Alignment.Center,
    divider: @Composable () -> Unit = { HorizontalDivider(thickness = 1.dp) },
    height: Dp = 192.dp
) {
    if (!expanded) return

    val horizontalAlignment = when (contentAlign) {
        Alignment.Center -> Alignment.CenterHorizontally
        Alignment.TopStart -> Alignment.Start
        else -> Alignment.CenterHorizontally
    }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .height(height)
                .verticalScroll(scrollState),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .clickable {
                            onItemSelected(item)
                            onDismiss()
                        },
                    contentAlignment = contentAlign
                ) {
                    itemContent(item, index)
                }
                if (index < items.size - 1) {
                    divider()
                }
            }
        }
    }
}
