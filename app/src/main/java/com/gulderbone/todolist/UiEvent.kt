package com.gulderbone.todolist

sealed class UiEvent {

    data class Navigate(val route: String) : UiEvent()
}
