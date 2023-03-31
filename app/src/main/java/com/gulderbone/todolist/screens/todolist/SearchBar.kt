package com.gulderbone.todolist.screens.todolist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
internal fun SearchBar(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = { query ->
            onValueChanged(query)
        },
        placeholder = {
            Text(text = "Search")
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        trailingIcon = {
            if (value.isNotBlank()) {
                IconButton(onClick = { onValueChanged("") }) {
                    Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
    )
}
