package com.gulderbone.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulderbone.todolist.TodoListEvent.OnAddTodoCLick
import com.gulderbone.todolist.TodoListEvent.OnDeleteToDo
import com.gulderbone.todolist.TodoListEvent.OnDoneChange
import com.gulderbone.todolist.TodoListEvent.OnTodoClick
import com.gulderbone.todolist.TodoListEvent.OnUndoDeleteClick
import com.gulderbone.todolist.data.Todo
import com.gulderbone.todolist.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository,
) : ViewModel() {

    private var deletedTodo: Todo? = null

    val todos: Flow<List<Todo>> = repository.getToDos()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: TodoListEvent) {
        when (event) {
            is OnTodoClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is OnAddTodoCLick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is OnDeleteToDo -> {
                viewModelScope.launch {
                    deletedTodo = event.todo
                    repository.deleteTodo(event.todo)
                    sendUiEvent(
                        UiEvent.ShowSnackbar(
                            message = "Todo deleted",
                            action = "Undo"
                        )
                    )
                }
            }
            is OnUndoDeleteClick -> {
                deletedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertToDo(todo)
                    }
                }
            }
            is OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertToDo(
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
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