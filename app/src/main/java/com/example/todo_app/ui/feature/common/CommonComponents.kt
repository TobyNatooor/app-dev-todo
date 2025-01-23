package com.example.todo_app.ui.feature.common

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Update
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
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import com.example.todo_app.model.ToDo
import com.example.todo_app.ui.theme.*
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.android.volley.toolbox.ImageLoader
import com.example.todo_app.BuildConfig.GIPHY_API_KEY
import com.example.todo_app.model.SortOption
import com.example.todo_app.model.ToDoStatus
import com.example.todo_app.repository.GifRepositoryImpl
import com.example.todo_app.ui.feature.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import java.net.HttpURLConnection
import java.net.URL

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
                .size(64.dp),
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
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
    modifier: Modifier = Modifier,
    selectAllText: Boolean = false,
    onTitleChange: (String) -> Unit,
    onRenameComplete: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val blankTitle = "Unnamed list"

    var isEnabled by remember { mutableStateOf(true) }
    var isFocused by remember { mutableStateOf(false) }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                title ?: "",
                TextRange((title ?: "").length)
            )
        )
    }

    Box(modifier) {
        LaunchedEffect(Unit) {
            isEnabled = true
            isFocused = false
            focusRequester.requestFocus()
            if (selectAllText) {
                textFieldValue = textFieldValue.copy(
                    selection = TextRange(0, textFieldValue.text.length)
                )
            }
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
            textStyle = textStyle ?: TextStyle(
                fontSize = 20.sp,
                color = neutral0,
                fontFamily = dosisFontFamily
            ),
            cursorBrush = SolidColor(neutral0),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .focusRequester(focusRequester)
                // Handle onTitleChange callback when unfocused
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
                }
                .onKeyEvent {
                    onTitleChange(textFieldValue.text)
                    true
                },
            enabled = isEnabled
        )

        if (textFieldValue.text.isBlank()) {
            Text(
                text = "Enter new title",
                style = (textStyle ?: TextStyle(
                    fontSize = 20.sp,
                    color = neutral0,
                    fontFamily = dosisFontFamily
                )).copy(color = neutral1)
            )
        }
    }
}

@Composable
fun DeleteDialog(
    id: Int,
    title: String,
    text: String,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        containerColor = primary0,
        titleContentColor = primary4,
        textContentColor = primary3,
        onDismissRequest = onDismiss,
        title = { Text(title, fontFamily = dosisFontFamily) },
        text = { Text(text, fontFamily = dosisFontFamily) },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary3,
                    contentColor = primary0
                ),
                onClick = {
                    onDelete(id)
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

@Composable
fun FavoriteButton(
    isFavorite: State<Boolean>,
    onFavClicked: () -> Unit,
) {

    val icon = if (isFavorite.value) Icons.Rounded.Star
    else Icons.Rounded.StarBorder

    val color = if (isFavorite.value) yellow2
    else neutral0

    IconButton(onClick = { onFavClicked() }) {
        Icon(icon, contentDescription = "Favorite", tint = color, modifier = Modifier.size(32.dp))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ToDoCheckBox(
    toDo: ToDo,
    viewModel: BaseViewModel,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier,
) {
    val color = when (toDo.status) {
        ToDoStatus.DONE -> green1
        ToDoStatus.IN_PROGRESS -> yellow1
        ToDoStatus.CANCELED -> red1
        else -> neutral1
    }
    val iconColor = when (toDo.status) {
        ToDoStatus.DONE -> green4
        ToDoStatus.IN_PROGRESS -> yellow4
        ToDoStatus.CANCELED -> red4
        else -> neutral1
    }
    val imageVector = when (toDo.status) {
        ToDoStatus.DONE -> Icons.Filled.Check
        ToDoStatus.IN_PROGRESS -> Icons.Filled.Update
        ToDoStatus.CANCELED -> Icons.Filled.Close
        else -> Icons.Filled.Check
    }
    Log.d("ABCDEF", "${toDo.title} ${imageVector}")

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        ChooseTodoStatus(viewModel, toDo, showDialog, size)
    }

    Box {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .padding(horizontal = 8.dp, vertical = 12.dp)
                .size(size)
                .background(
                    color = color,
                    shape = RoundedCornerShape(5.dp)
                )
                .combinedClickable(
                    onClick = {
                        viewModel.updateToDoItem(
                            toDo.copy(status = toDo.status.check())
                        )
                    },
                    onLongClick = {
                        showDialog.value = true
                    },
                ),
        )
        Icon(
            imageVector = imageVector,
            contentDescription = "Check Icon",
            tint = iconColor,
            modifier = Modifier
                .size(size * 1.1f)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun ChooseTodoStatus(
    viewModel: BaseViewModel,
    toDo: ToDo,
    showDialog: MutableState<Boolean>,
    size: Dp,
) {
    AlertDialog(
        containerColor = primary2,
        onDismissRequest = { showDialog.value = false },
        title = {},
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = neutral1,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.NOT_DONE)
                                )
                                showDialog.value = false
                            }
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = green2,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.DONE)
                                )
                                showDialog.value = false
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Check Icon",
                            tint = green4,
                            modifier = Modifier
                                .size(size * 1.1f)
                                .align(Alignment.Center)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = yellow2,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.IN_PROGRESS)
                                )
                                showDialog.value = false
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Update,
                            contentDescription = "Check Icon",
                            tint = yellow4,
                            modifier = Modifier
                                .size(size * 1.1f)
                                .align(Alignment.Center)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = red2,
                                shape = RoundedCornerShape(5.dp)
                            )
                            .clickable {
                                viewModel.updateToDoItem(
                                    toDo.copy(status = ToDoStatus.CANCELED)
                                )
                                showDialog.value = false
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Check Icon",
                            tint = red4,
                            modifier = Modifier
                                .size(size * 1.1f)
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun SortButton(
    onSortClicked: ((SortOption) -> Unit)? = null,
    sortOptions: List<SortOption>,
) {
    var expanded by remember { mutableStateOf(false) }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = neutral4
        ),
        onClick = { expanded = true }
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Sort,
            contentDescription = "Sort",
            tint = neutral1,
            modifier = Modifier.size(32.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(neutral1)
        ) {
            Text(
                text = "Sort by",
                color = neutral4,
                fontSize = 16.sp,
                fontFamily = dosisFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            HorizontalDivider(
                color = neutral4,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            sortOptions.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onSortClicked?.invoke(option)
                        expanded = false
                    },
                    text = {
                        Text(
                            text = option.toString(),
                            color = neutral4,
                            fontSize = 16.sp,
                            fontFamily = dosisFontFamily,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier
                        .background(Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun SearchButton(
    onSearchClicked: ((String) -> Unit)? = null,
    getQuery: () -> String,
) {
    val showSearchField = remember { mutableStateOf(getQuery() != "") }
    val seachFieldFocus = remember { mutableStateOf(getQuery() != "") }
    Row(
        modifier = Modifier
            .padding(bottom = 8.dp)
    ) {
        IconButton(onClick = {
            if (!showSearchField.value) {
                seachFieldFocus.value = true
                showSearchField.value = true
                onSearchClicked?.invoke(" ")
            }
        }) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                tint = neutral1,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp)
            )
        }
        if (showSearchField.value) {
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    var textField by remember {
                        mutableStateOf(
                            TextFieldValue(
                                text = getQuery().trimStart(),
                                selection = TextRange(getQuery().trimStart().length)
                            )
                        )
                    }
                    BasicTextField(
                        value = textField,
                        onValueChange = { newText ->
                            textField = newText
                            onSearchClicked?.invoke(if (newText.text.isBlank()) " " else newText.text.trimStart())
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = neutral1,
                            fontFamily = dosisFontFamily
                        ),
                        cursorBrush = SolidColor(neutral1),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onSearchClicked?.invoke(textField.text.trimStart())
                                seachFieldFocus.value = false
                                focusManager.clearFocus()
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (showSearchField.value && !focusState.isFocused && seachFieldFocus.value) {
                                    focusRequester.requestFocus()
                                } else if (focusState.isFocused && !seachFieldFocus.value) {
                                    seachFieldFocus.value = true
                                }
                            }
                    )
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            showSearchField.value = false
                            onSearchClicked?.invoke("")
                        },
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            tint = neutral1,
                            modifier = Modifier
                                .padding(bottom = 10.dp)
                                .fillMaxHeight()
                        )
                    }
                }
                HorizontalDivider(
                    thickness = 2.dp,
                    color = neutral1,
                    modifier = Modifier
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            bottom = 6.dp
                        )
                )
            }
        }
    }
}

@Composable
fun GiphyDialog() {
    var gifUrl by remember { mutableStateOf<String?>(null) }
    var gifStatus by remember { mutableStateOf<Int?>(null) }
    var showDialog by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        GifRepositoryImpl().getRandomCongratulationGif { response ->
            gifUrl = response.body()?.data?.images?.original?.url ?: ""
            gifStatus = response.body()?.meta?.status
        }
    }

    if (showDialog && gifStatus != null)
        if (gifUrl != null && gifStatus == 200) {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                },
            ) {
                Card(
                    modifier = Modifier.aspectRatio(1f)
                ) {
                    Text(
                        "Congrats!\nYou've completed all your todos!",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        style = TextStyle(
                            fontSize = 20.sp,
                            color = neutral0,
                            fontFamily = dosisFontFamily
                        ),
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        if (SDK_INT >= 28) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(gifUrl)
                                    .decoderFactory(ImageDecoderDecoder.Factory())
                                    .build(),
                                contentDescription = "gif",
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                        } else {
                            Text(
                                "\uD83D\uDC4D",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterStart)
                                    .wrapContentHeight(),
                                style = TextStyle(
                                    fontSize = 160.sp,
                                    color = neutral0,
                                    fontFamily = dosisFontFamily
                                ),
                            )
                        }
                    }
                }
            }
        } else {
            Dialog(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                Card(
                    modifier = Modifier.aspectRatio(1f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            "Error",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            style = TextStyle(
                                color = neutral0,
                                fontFamily = dosisFontFamily
                            ),
                        )
                    }
                }
            }
        }
}
