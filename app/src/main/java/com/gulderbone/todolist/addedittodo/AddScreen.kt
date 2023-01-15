package com.gulderbone.todolist.addedittodo

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AddScreen(
    viewModel: AddEditTodoViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->

        }
    }

    BasicTextField(value = viewModel.state.title ?: "", onValueChange = {

    })
    Button(onClick = {

    }) {

    }
}