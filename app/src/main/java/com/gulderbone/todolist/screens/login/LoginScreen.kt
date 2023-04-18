package com.gulderbone.todolist.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gulderbone.todolist.UiEvent
import com.gulderbone.todolist.screens.login.LoginEvent.OnEmailChange
import com.gulderbone.todolist.screens.login.LoginEvent.OnPasswordChange
import com.gulderbone.todolist.screens.login.LoginEvent.OnSignIn

@Composable
fun LoginScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = FocusRequester()

    LaunchedEffect(key1 = true) {
        focusRequester.requestFocus()
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                is UiEvent.PopBackStack -> TODO()
                is UiEvent.ShowSnackbar -> TODO()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Compose Todos",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        EmailTextField(focusRequester, viewModel, focusManager)
        PasswordTextField(viewModel)
        SignInButton(viewModel)
        TextButton(
            onClick = { /* Handle forgot password */ },
        ) {
            Text(text = "Forgot password?")
        }
        Text(
            text = "Don't have an account?",
            modifier = Modifier.padding(top = 32.dp)
        )
        TextButton(
            onClick = { /* Handle sign up */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Sign up")
        }
        Text(text = "© 2023 Compose Todos. All rights reserved.")
    }
}

@Composable
private fun EmailTextField(
    focusRequester: FocusRequester,
    viewModel: LoginViewModel,
    focusManager: FocusManager,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .focusRequester(focusRequester),
        value = viewModel.state.email,
        onValueChange = {
            viewModel.onEvent(OnEmailChange(it))
        },
        label = { Text(text = "Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.moveFocus(FocusDirection.Next)
            }
        )
    )
}

@Composable
private fun PasswordTextField(viewModel: LoginViewModel) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = viewModel.state.password,
        onValueChange = {
            viewModel.onEvent(OnPasswordChange(it))
        },
        singleLine = true,
        label = { Text(text = "Password") }
    )
}

@Composable
private fun SignInButton(viewModel: LoginViewModel) {
    ElevatedButton(
        onClick = {
            viewModel.onEvent(OnSignIn)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Sign in")
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    LoginScreen(
        onNavigate = {},
        viewModel = LoginViewModel(),
    )
}
