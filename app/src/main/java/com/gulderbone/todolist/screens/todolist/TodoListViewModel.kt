package com.gulderbone.todolist.screens.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulderbone.todolist.Routes
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnAddTodoCLick
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnDeleteToDo
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnDoneChange
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnSearchQuery
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnTodoClick
import com.gulderbone.todolist.screens.todolist.TodoListEvent.OnUndoDeleteClick
import com.gulderbone.todolist.UiEvent
import com.gulderbone.todolist.data.Todo
import com.gulderbone.todolist.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private var deletedTodo: Todo? = null

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val todos: Flow<List<Todo>> = todoRepository.getToDos()
    val filteredTodos = searchQuery
        .combine(todos) { query, todos ->
            if (query.isBlank()) {
                todos
            } else {
                todos.filter {
                    it.doesMatchSearchQuery(query)
                }
            }
        }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: TodoListEvent) {
        when (event) {
            is OnSearchQuery -> {
                _searchQuery.value = event.query
            }
            is OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is OnDoneChange -> {
                viewModelScope.launch {
                    todoRepository.insertToDo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
            is OnDeleteToDo -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    todoRepository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "${event.todo.title} deleted",
                            action = "Undo"
                        )
                    )
                }
            }
            is OnAddTodoCLick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        todoRepository.insertToDo(todo)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}