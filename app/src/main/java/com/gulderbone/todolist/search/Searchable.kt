package com.gulderbone.todolist.search

internal interface Searchable {

    fun doesMatchSearchQuery(query: String): Boolean
}
