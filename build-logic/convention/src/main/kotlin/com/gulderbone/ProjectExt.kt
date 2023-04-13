package com.gulderbone

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.tasks.TaskContainer

inline fun <reified T> ExtensionContainer.configure(noinline action: T.() -> Unit) {
    configure(T::class.java) { action(it) }
}

inline fun <reified T : Task> TaskContainer.withType(): NamedDomainObjectSet<T> =
    withType(T::class.java)

inline fun <reified T> ExtensionContainer.getByType(): T =
    getByType(T::class.java)
