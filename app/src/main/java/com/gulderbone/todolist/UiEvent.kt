package com.gulderbone.todolist

sealed class UiEvent {

    data class Navigate(val route: String) : UiEvent()

    data class ShowSnackbar(
        val message: String,
        val action: String? = null,
    ) : UiEvent()

    object PopBackStack : UiEvent()
}
