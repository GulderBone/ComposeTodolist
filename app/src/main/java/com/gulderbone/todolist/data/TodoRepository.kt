package com.gulderbone.todolist.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun insertToDo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getToDoById(id: Int): Todo?

    fun getToDos(): Flow<List<Todo>>
}
