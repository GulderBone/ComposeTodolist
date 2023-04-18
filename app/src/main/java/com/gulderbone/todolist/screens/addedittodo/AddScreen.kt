package com.gulderbone.todolist.screens.addedittodo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gulderbone.todolist.UiEvent.PopBackStack
import com.gulderbone.todolist.UiEvent.ShowSnackbar
import com.gulderbone.todolist.screens.addedittodo.AddEditTodoEvent.OnDescriptionChange
import com.gulderbone.todolist.screens.addedittodo.AddEditTodoEvent.OnSaveTodoClick
import com.gulderbone.todolist.screens.addedittodo.AddEditTodoEvent.OnTitleChange

@Composable
fun AddScreen(
    viewModel: AddEditTodoViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ShowSnackbar -> snackbarHostState.showSnackbar(
                    message = event.message,
                    actionLabel = event.action,
                )
                is PopBackStack -> onPopBackStack()
                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                TitleTextField(viewModel, focusRequester, focusManager)
                Spacer(modifier = Modifier.height(8.dp))
                DescriptionTextField(viewModel, focusManager)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(OnSaveTodoClick)
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save",
                )
            }
        }
    )
}

@Composable
private fun TitleTextField(
    viewModel: AddEditTodoViewModel,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = viewModel.state.title,
        onValueChange = {
            if (it.length < 50) {
                viewModel.onEvent(OnTitleChange(it))
            }
        },
        placeholder = {
            Text(text = "Title")
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        )
    )
}

@Composable
private fun DescriptionTextField(
    viewModel: AddEditTodoViewModel,
    focusManager: FocusManager,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = viewModel.state.description,
        onValueChange = {
            if (it.length < 200) {
                viewModel.onEvent(OnDescriptionChange(it))
            }
        },
        placeholder = {
            Text(text = "Description")
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                viewModel.onEvent(OnSaveTodoClick)
            }
        ),
    )
}
