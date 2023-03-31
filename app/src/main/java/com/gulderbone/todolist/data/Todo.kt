package com.gulderbone.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gulderbone.todolist.search.Searchable

@Entity
data class Todo(
    // TODO Should be TodoDto and have a separate Todo (entity class)
    val title: String,
    val description: String?,
    val isDone: Boolean,
    @PrimaryKey val id: Int? = null,
) : Searchable {

    override fun doesMatchSearchQuery(query: String): Boolean =
        title.contains(query, ignoreCase = true)
}
