package com.gulderbone.todolist.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gulderbone.todolist.Routes
import com.gulderbone.todolist.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state get() = _state.value

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> {
                _state.value = state.copy(
                    email = event.email,
                )
            }

            is LoginEvent.OnPasswordChange -> {
                _state.value = state.copy(
                    password = event.password,
                )
            }

            LoginEvent.OnSignIn -> {
                sendUiEvent(UiEvent.Navigate(Routes.TODO_LIST))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
