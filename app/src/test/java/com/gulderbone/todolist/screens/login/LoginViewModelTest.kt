package com.gulderbone.todolist.screens.login

import com.gulderbone.todolist.InstantTaskExecutorExtension
import com.gulderbone.todolist.Routes
import com.gulderbone.todolist.UiEvent
import com.gulderbone.todolist.observeTillTeardown
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
internal class LoginViewModelTest {

    @Nested
    @DisplayName("When event received")
    inner class OnEventReceived {

        @Test
        fun `when event is OnEmailChange, then updates state with the inserted email`() {
            val fakeEmail = "tested-email"
            val fakeEvent = LoginEvent.OnEmailChange(
                email = fakeEmail
            )

            val viewModel = newViewModel()

            viewModel.onEvent(fakeEvent)

            assertEquals(
                fakeEmail,
                viewModel.state.email,
            )
        }

        @Test
        fun `when event is OnPasswordChange, then updates state with the inserted password`() {
            val fakePassword = "tested-password"
            val fakeEvent = LoginEvent.OnPasswordChange(
                password = fakePassword
            )

            val viewModel = newViewModel()

            viewModel.onEvent(fakeEvent)

            assertEquals(
                fakePassword,
                viewModel.state.password,
            )
        }

        @Test
        fun `when event is OnSignIn, then sends navigate ui event to todo list`() = runTest {
            val fakeEvent = LoginEvent.OnSignIn

            val events = mutableListOf<UiEvent>()
            val viewModel = newViewModel().apply {
                uiEvent.observeTillTeardown(events::add)
            }

            viewModel.onEvent(fakeEvent)
            advanceUntilIdle()

            val expected = listOf(UiEvent.Navigate(Routes.TODO_LIST))

            assertEquals(
                expected,
                events,
            )
        }
    }

    private fun newViewModel(): LoginViewModel =
        LoginViewModel()
}
