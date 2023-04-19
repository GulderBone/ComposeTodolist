package com.gulderbone.todolist.screens.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gulderbone.todolist.UiEvent.Navigate
import com.gulderbone.todolist.UiEvent.PopBackStack
import com.gulderbone.todolist.UiEvent.ShowSnackbar
import com.gulderbone.todolist.data.Todo
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnSearchQuery
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnTodoClick
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnUndoDeleteClick

@Composable
fun TodoListScreen(
    onNavigate: (Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel(),
) {
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()
    val notDoneTodos = viewModel.notDoneTodos.collectAsStateWithLifecycle(initialValue = emptyList())
    val doneTodos = viewModel.doneTodos.collectAsStateWithLifecycle(initialValue = emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is Navigate -> onNavigate(event)
                is PopBackStack -> TODO()
                is ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SearchBar(
                    value = searchQuery.value,
                    onValueChanged = {
                        viewModel.onEvent(OnSearchQuery(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TodoItemList(notDoneTodos, viewModel)
                TodoItemListsDivider(notDoneTodos, doneTodos)
                TodoItemList(doneTodos, viewModel)
            }
        }
    }
}

@Composable
private fun TodoItemListsDivider(
    notDoneTodos: State<List<Todo>>,
    doneTodos: State<List<Todo>>,
) {
    if (notDoneTodos.value.isNotEmpty() && doneTodos.value.isNotEmpty()) {
        Divider()
    }
}

@Composable
private fun TodoItemList(
    todos: State<List<Todo>>,
    viewModel: TodoListViewModel,
) {
    LazyColumn {
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
