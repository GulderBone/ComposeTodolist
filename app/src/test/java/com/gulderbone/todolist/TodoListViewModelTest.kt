package com.gulderbone.todolist

import com.gulderbone.todolist.TodoListEvent.OnAddTodoCLick
import com.gulderbone.todolist.TodoListEvent.OnDeleteToDo
import com.gulderbone.todolist.TodoListEvent.OnDoneChange
import com.gulderbone.todolist.TodoListEvent.OnSearchQuery
import com.gulderbone.todolist.TodoListEvent.OnTodoClick
import com.gulderbone.todolist.TodoListEvent.OnUndoDeleteClick
import com.gulderbone.todolist.data.TodoRepository
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@ExtendWith(InstantTaskExecutorExtension::class)
internal class TodoListViewModelTest {

    @Nested
    @DisplayName("When event received")
    inner class OnEventReceived {

        @Test
        fun `when event is OnSearchQuery, then updates search query`() {
            val fakeQuery = "tested-query"
            val fakeEvent = OnSearchQuery(fakeQuery)

            val viewModel = newViewModel()

            viewModel.onEvent(fakeEvent)

            val expected = "tested-query"

            assertEquals(
                expected,
                viewModel.searchQuery.value,
            )
        }

        @Test
        fun `when event is OnTodoClick, then sends navigate ui event`() = runTest {
            val fakeEvent = OnTodoClick(fakeTodo)

            val events = mutableListOf<UiEvent>()
            val viewModel = newViewModel().apply {
                uiEvent.observeTillTeardown(events::add)
            }

            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            val expected = listOf(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${fakeEvent.todo.id}"))

            assertEquals(
                expected,
                events,
            )
        }

        @Test
        fun `when event is OnDoneChange, then inserts todo`() = runTest {
            val fakeEvent = OnDoneChange(
                todo = fakeTodo,
                isDone = true,
            )
            val mockTodoRepository = mock<TodoRepository>()

            val viewModel = newViewModel(
                todoRepository = mockTodoRepository,
            )

            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            verify(mockTodoRepository).insertToDo(
                fakeTodo.copy(
                    isDone = true
                )
            )
        }

        @Test
        fun `when event is OnDeleteToDo, then deletes todo and shows snackbar`() = runTest {
            val fakeEvent = OnDeleteToDo(
                todo = fakeTodo,
            )
            val mockTodoRepository = mock<TodoRepository>()

            val events = mutableListOf<UiEvent>()
            val viewModel = newViewModel(
                todoRepository = mockTodoRepository,
            ).apply {
                uiEvent.observeTillTeardown(events::add)
            }

            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            val expected = listOf(
                UiEvent.ShowSnackbar(
                    message = "${fakeEvent.todo.title} deleted",
                    action = "Undo"
                )
            )

            assertEquals(
                expected,
                events,
            )
            verify(mockTodoRepository).deleteTodo(fakeTodo)
        }

        @Test
        fun `when event is OnAddTodoCLick, then navigates to add edit todo screen`() = runTest {
            val fakeEvent = OnAddTodoCLick

            val events = mutableListOf<UiEvent>()
            val viewModel = newViewModel().apply {
                uiEvent.observeTillTeardown(events::add)
            }

            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            val expected = listOf(
                UiEvent.Navigate(Routes.ADD_EDIT_TODO)
            )

            assertEquals(
                expected,
                events,
            )
        }

        @Test
        fun `when event is OnUndoDeleteClick, then inserts the previously deleted todo`() = runTest {
            val fakeEvent = OnUndoDeleteClick
            val mockTodoRepository = mock<TodoRepository>()

            val viewModel = newViewModel(
                todoRepository = mockTodoRepository,
            )
            viewModel.onEvent(OnDeleteToDo(fakeTodo))
            advanceUntilIdle()
            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            verify(mockTodoRepository).insertToDo(fakeTodo)
        }

        @Test
        fun `when event is OnUndoDeleteClick but there is no deleted todo, then does nothing`() = runTest {
            val fakeEvent = OnUndoDeleteClick
            val mockTodoRepository = mock<TodoRepository>()

            val viewModel = newViewModel(
                todoRepository = mockTodoRepository,
            )
            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            verify(mockTodoRepository).getToDos()
            verifyNoMoreInteractions(mockTodoRepository)
        }
    }

    private fun newViewModel(
        todoRepository: TodoRepository = mock(),
    ): TodoListViewModel =
        TodoListViewModel(
            todoRepository = todoRepository,
        )
}