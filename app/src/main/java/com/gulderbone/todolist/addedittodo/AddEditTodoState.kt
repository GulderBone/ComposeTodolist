package com.gulderbone.todolist.addedittodo

import com.gulderbone.todolist.data.Todo

data class AddEditTodoState(
    val todo: Todo? = null,
    val title: String? = "",
    val description: String = "",
)