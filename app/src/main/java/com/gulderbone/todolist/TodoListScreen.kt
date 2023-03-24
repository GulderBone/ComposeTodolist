package com.gulderbone.todolist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gulderbone.todolist.TodoListEvent.OnDoneChange
import com.gulderbone.todolist.TodoListEvent.OnTodoClick
import com.gulderbone.todolist.TodoListEvent.OnUndoDeleteClick
import com.gulderbone.todolist.UiEvent.Navigate
import com.gulderbone.todolist.UiEvent.PopBackStack
import com.gulderbone.todolist.UiEvent.ShowSnackbar
import com.gulderbone.todolist.data.Todo

@Composable
fun TodoListScreen(
    onNavigate: (Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel(),
) {
    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is Navigate -> onNavigate(event)
                is PopBackStack -> TODO()
                is ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(OnUndoDeleteClick)
                    }
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(TodoListEvent.OnAddTodoCLick)
                }) {
                Icon(Icons.Filled.Add, contentDescription = "Add To Do")
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier.padding(padding),
            color = MaterialTheme.colors.background,
        ) {
            LazyColumn(
                Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(todos.value) { todo ->
                    TodoItem(
                        todo = todo,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(OnTodoClick(todo))
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (TodoListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(0.8f),
            text = todo.title,
        )
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { isChecked ->
                onEvent(OnDoneChange(todo, isChecked))
            },
        )
        IconButton(
            onClick = { onEvent(TodoListEvent.OnDeleteToDo(todo)) },
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "delete Todo",
            )
        }
    }
}