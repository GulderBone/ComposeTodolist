package com.gulderbone.todolist

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class InstantTaskExecutorExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext?) {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    override fun afterAll(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }

    companion object {

        val tearDownQueue: MutableList<() -> Unit> = mutableListOf()
    }
}

fun <T> Flow<T>.observeTillTeardown(onUpdate: ((T) -> Unit)? = null) {
    val scope = TestScope(UnconfinedTestDispatcher())
    onEach { onUpdate?.invoke(it) }
        .launchIn(scope)

    InstantTaskExecutorExtension.tearDownQueue.add { scope.cancel() }
}