package com.gulderbone.todolist.screens.addedittodo

import com.gulderbone.todolist.data.Todo

data class AddEditTodoState(
    val todo: Todo? = null,
    val title: String = "",
    val description: String = "",
)