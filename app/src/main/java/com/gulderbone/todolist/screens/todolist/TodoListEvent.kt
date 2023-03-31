package com.gulderbone.todolist.screens.todolist

import com.gulderbone.todolist.data.Todo

sealed interface TodoListEvent {

    data class OnSearchQuery(val query: String) : TodoListEvent

    data class OnTodoClick(val todo: Todo) : TodoListEvent

    data class OnDoneChange(val todo: Todo, val isDone: Boolean) : TodoListEvent

    data class OnDeleteToDo(val todo: Todo) : TodoListEvent

    object OnAddTodoCLick : TodoListEvent

    object OnUndoDeleteClick : TodoListEvent
}
