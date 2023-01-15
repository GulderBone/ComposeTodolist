package com.gulderbone.todolist

import com.gulderbone.todolist.data.Todo

sealed class TodoListEvent {

    data class OnDeleteToDo(val todo: Todo) : TodoListEvent()

    data class OnDoneChange(val todo: Todo, val isDone: Boolean) : TodoListEvent()

    data class OnTodoClick(val todo: Todo) : TodoListEvent()

    object OnAddTodoCLick : TodoListEvent()

    object OnUndoDeleteClick : TodoListEvent()
}