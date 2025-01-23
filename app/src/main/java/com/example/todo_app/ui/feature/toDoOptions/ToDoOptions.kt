package com.example.todo_app.ui.feature.toDoOptions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo_app.model.CheckList
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.feature.common.CustomDropdownMenu
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.example.todo_app.ui.theme.*
import com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom
import com.google.android.libraries.places.api.model.Place
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import com.example.todo_app.ui.feature.common.DeleteDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoOptions(
    toDo: ToDo,
    checklists: List<CheckList>,
    viewmodel: ToDoOptionsViewModel,
    cameraPositionState: CameraPositionState,
    getLocation: ((Place?) -> Unit?) -> Unit,
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    navController: NavHostController
) {
    var markerPosition = remember { LatLng(0.0, 0.0) }
    val markerState = remember { MarkerState(position = markerPosition) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deadline by remember {
        mutableStateOf(if (toDo.deadline == null) "" else formatDeadline(toDo.deadline))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            //verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Text(
                    color = neutral1,
                    fontFamily = dosisFontFamily,
                    text = "Edit ${toDo.title}",
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontSize = 54.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 100.dp, bottom = 100.dp)
                )
            }
            stickyHeader {
                appBar()
            }
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = neutral2,
                    ),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .fillMaxSize()
                    ) {
                        Option(
                            optionTitle = "Todo title",
                            content = {
                                TextFieldOption(
                                    startText = toDo.title.toString(),
                                    hintText = "Enter todo title",
                                    height = 42.dp,
                                    contentAlign = Alignment.Center,
                                    onTextChanged = { title -> viewmodel.updateToDo(toDo.copy(title = title)) }
                                )
                            }
                        )
                        Option(
                            optionTitle = "Move to list",
                            content = {
                                DropdownMenuOption(
                                    hintText = checklists.find { it.id == toDo.listId }?.title
                                        ?: "Select a list",
                                    height = 42.dp,
                                    contentAlign = Alignment.Center,
                                    sortOptions = checklists.filter { it.id != toDo.listId }
                                        .map { DropdownOptionItem(it.id, it.title.toString()) },
                                    onOptionSelected = { selectedOption ->
                                        val updatedToDo = toDo.copy(listId = selectedOption.id)
                                        viewmodel.updateToDo(updatedToDo)
                                    }
                                )
                            }
                        )
                        if (showDatePicker) {
                            DatePickerModal(
                                onDateSelected = { selectedDate ->
                                    if (selectedDate != null) {
                                        val date = Date(selectedDate)
                                        val formattedDate = SimpleDateFormat(
                                            "yyyy-MM-dd",
                                            Locale.getDefault()
                                        ).format(date)
                                        val localDate = LocalDate.parse(
                                            formattedDate,
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                        )
                                        val localDateTime =
                                            LocalDateTime.of(localDate, LocalTime.of(0, 0, 0))
                                        deadline = formatDeadline(localDateTime)
                                        viewmodel.updateToDo(toDo.copy(deadline = localDateTime))
                                    }
                                    showDatePicker = false
                                },
                                onDismiss = { showDatePicker = false }
                            )
                        }
                        Option(
                            optionTitle = "Deadline",
                            content = {
                                TextFieldOption(
                                    startText = deadline,
                                    hintText = "Select a date",
                                    height = 42.dp,
                                    contentAlign = Alignment.TopStart,
                                    onFocusChanged = { isFocused ->
                                        if (isFocused) {
                                            showDatePicker = true
                                        }
                                    },
                                    onTextChanged = { }
                                )
                            },
                        )
                        Option(
                            optionTitle = "Description",
                            content = {
                                TextFieldOption(
                                    startText = toDo.description,
                                    hintText = "Enter todo description",
                                    height = 150.dp,
                                    contentAlign = Alignment.TopStart,
                                    onTextChanged = { description ->
                                        viewmodel.updateToDo(
                                            toDo.copy(
                                                description = description
                                            )
                                        )
                                    }
                                )
                            }
                        )
                        val text = remember { mutableStateOf(toDo.location ?: "") }
                        Option(
                            optionTitle = "Location",
                            content = {
                                TextFieldOption(
                                    text.value,
                                    textState = text,
                                    hintText = "Enter todo address",
                                    height = 42.dp,
                                    contentAlign = Alignment.TopStart,
                                    onTextChanged = { text.value = it },
                                    onFocusChanged = { isFocused ->
                                        if (isFocused) {
                                            getLocation { place ->
                                                if (place != null) {
                                                    val name = place.displayName
                                                    val latitude = place.location?.latitude ?: 0.0
                                                    val longitude = place.location?.longitude ?: 0.0
                                                    markerPosition = LatLng(latitude, longitude)
                                                    markerState.position = markerPosition
                                                    cameraPositionState.position =
                                                        fromLatLngZoom(markerPosition, 10f)
                                                    viewmodel.updateToDo(
                                                        toDo.copy(
                                                            location = name,
                                                            latitude = latitude,
                                                            longitude = longitude,
                                                        )
                                                    )
                                                    if (name != null) {
                                                        text.value = name
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        )
                        if (toDo.location != null && toDo.latitude != null && toDo.longitude != null) {
                            Box()
                            {
                                GoogleMap(
                                    modifier = Modifier
                                        //.weight(1f)
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(12.dp)),
                                    cameraPositionState = cameraPositionState
                                ) {
                                    // Add a marker
                                    markerPosition = LatLng(toDo.latitude, toDo.longitude)
                                    markerState.position = markerPosition
                                    cameraPositionState.position =
                                        fromLatLngZoom(markerPosition, 10f)
                                    Marker(
                                        title = toDo.location,
                                        state = markerState
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .background(
                                        color = primary0,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ) {
                                Text(
                                    "Specify location to view map",
                                    textAlign = TextAlign.Center,
                                    fontFamily = dosisFontFamily,
                                    color = primary4,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(Alignment.Center)
                                        .wrapContentHeight()
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 20.dp)
                        ) {
                            if (showDeleteDialog)
                                DeleteDialog(
                                    id = toDo.id,
                                    title = "Delete todo \"${toDo.title}\"?",
                                    text = "Are you sure you want to delete this todo?",
                                    onDelete = {
                                        showDeleteDialog = true
                                        viewmodel.deleteToDo(toDo)
                                        navController.popBackStack()
                                    },
                                    onDismiss = {
                                        showDeleteDialog = false
                                    }
                                )
                            Button(
                                onClick = {
                                    showDeleteDialog = true
                                },
                                colors = ButtonColors(red2, red0, red0, red0)
                            ) { Text("Delete") }
                            Button(
                                onClick = {
                                    navController.popBackStack()
                                },
                                colors = ButtonColors(primary3, primary0, red0, red0)
                            ) { Text("Done") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Option(
    optionTitle: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Text(
            text = optionTitle,
            color = neutral0,
            fontSize = 16.sp,
            fontFamily = dosisFontFamily,
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
    onFocusChanged: ((Boolean) -> Unit)? = null,
    textState: MutableState<String> = remember { mutableStateOf(startText) },
    contentAlign: Alignment
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
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
            .background(
                color = primary0,
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = modifier
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    if (onFocusChanged == null) {
                        onFocusChange(state.isFocused)
                    } else {
                        onFocusChanged(state.isFocused)
                    }
                },

            cursorBrush = SolidColor(primary4),
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontFamily = dosisFontFamily,
                color = primary4,
                textAlign = textAlign
            ),
            decorationBox = @Composable { innerTextField ->
                Box(
                    contentAlignment = contentAlign,
                    modifier = modifier
                        .padding(10.dp)
                ) {
                    if (textState.value.isEmpty() && !focusState.value) {
                        Text(
                            text = hintText,
                            fontSize = 18.sp,
                            fontFamily = dosisFontFamily,
                            color = primary1,
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

@Composable
private fun DropdownMenuOption(
    hintText: String,
    height: Dp,
    sortOptions: List<DropdownOptionItem>,
    onOptionSelected: (DropdownOptionItem) -> Unit,
    contentAlign: Alignment = Alignment.Center
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption: DropdownOptionItem? by remember { mutableStateOf(null) }
    val shape = RoundedCornerShape(12.dp)
    val textAlign = when (contentAlign) {
        Alignment.Center -> TextAlign.Center
        Alignment.TopStart -> TextAlign.Left
        else -> TextAlign.Center
    }
    val modifier = Modifier.fillMaxWidth()

    Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
        Box(
            modifier = modifier
                .height(height)
                .background(color = primary0, shape = shape)
                .clickable { expanded = !expanded },
            contentAlignment = contentAlign
        ) {
            Text(
                text = selectedOption?.title ?: hintText,
                fontSize = 18.sp,
                fontFamily = dosisFontFamily,
                color = primary4,
                textAlign = textAlign,
                modifier = Modifier.padding(10.dp)
            )
        }

        CustomDropdownMenu(
            modifier = modifier
                .background(color = primary0, shape = shape)
                .padding(vertical = 4.dp),
            divider = {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = primary4,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            contentAlign = contentAlign,
            expanded = expanded,
            items = sortOptions,
            onDismiss = { expanded = false },
            onItemSelected = {
                selectedOption = it
                onOptionSelected(it)
            },
            itemContent = { item, index ->
                Box(
                    modifier = modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                    contentAlignment = contentAlign
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontFamily = dosisFontFamily,
                        color = primary4,
                        textAlign = textAlign
                    )
                }
            }
        )
    }
}

// https://developer.android.com/develop/ui/compose/components/datepickers
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    ) {
        DatePicker(state = datePickerState)
    }
}

fun formatDeadline(deadline: LocalDateTime): String {
    return deadline.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
}

data class DropdownOptionItem(
    val id: Int,
    val title: String
)
