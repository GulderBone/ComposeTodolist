package com.gulderbone.todolist.screens.addedittodo

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulderbone.todolist.UiEvent
import com.gulderbone.todolist.data.Todo
import com.gulderbone.todolist.data.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = mutableStateOf(AddEditTodoState())
    val state get() = _state.value

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getToDoById(todoId)?.let { todo ->
                    _state.value = state.copy(
                        todo = todo,
                        title = todo.title,
                        description = todo.description ?: ""
                    )
                }
            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnTitleChange -> {
                _state.value = state.copy(
                    title = event.title,
                )
            }
            is AddEditTodoEvent.OnDescriptionChange -> {
                _state.value = state.copy(
                    description = event.description,
                )
            }
            is AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if (state.title.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                message = "The title can't be empty"
                            )
                        )
                        return@launch
                    }
                    repository.insertToDo(
                        Todo(
                            title = state.title,
                            description = state.description,
                            isDone = state.todo?.isDone ?: false,
                            id = state.todo?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
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
