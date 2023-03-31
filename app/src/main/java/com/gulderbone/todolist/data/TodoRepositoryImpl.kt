package com.gulderbone.todolist.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
) : TodoRepository {

    override suspend fun insertToDo(todo: Todo) = todoDao.insertToDo(todo)

    override suspend fun deleteTodo(todo: Todo) = todoDao.deleteTodo(todo)

    override suspend fun getToDoById(id: Int): Todo? = todoDao.getToDoById(id)

    override fun getToDos(): Flow<List<Todo>> = todoDao.getToDos()
}
