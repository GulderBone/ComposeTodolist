package com.gulderbone.todolist.screens.addedittodo

sealed interface AddEditTodoEvent {

    data class OnTitleChange(val title: String) : AddEditTodoEvent

    data class OnDescriptionChange(val description: String) : AddEditTodoEvent

    object OnSaveTodoClick : AddEditTodoEvent
}
