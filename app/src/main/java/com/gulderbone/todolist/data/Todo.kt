package com.gulderbone.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo( // TODO Should be TodoDto and have a separate Todo (entity class)
    val title: String,
    val description: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null,
)
