package com.gulderbone.todolist.screens.login

sealed interface LoginEvent {

    data class OnEmailChange(val email: String) : LoginEvent

    data class OnPasswordChange(val password: String) : LoginEvent

    object OnSignIn : LoginEvent
}
