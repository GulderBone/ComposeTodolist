package com.gulderbone.todolist.screens.todolist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
    val searchQuery = viewModel.searchQuery.collectAsState()
    val todos = viewModel.filteredTodos.collectAsState(initial = emptyList())
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
                TodoItemList(todos, viewModel)
            }
        }
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
