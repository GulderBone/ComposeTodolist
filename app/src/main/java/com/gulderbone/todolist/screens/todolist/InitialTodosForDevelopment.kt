@file:Suppress("MagicNumber")
package com.gulderbone.todolist.screens.todolist

import com.gulderbone.todolist.data.Todo

val developmentTodos: List<Todo> = List(100) {
    Todo(
        title = "$it",
        description = "hehe",
        isDone = false,
    )
}
