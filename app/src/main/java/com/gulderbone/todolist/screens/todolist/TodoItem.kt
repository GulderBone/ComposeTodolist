package com.gulderbone.todolist.screens.todolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.gulderbone.todolist.data.Todo

@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (TodoListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textDecoration = if (todo.isDone) {
        TextDecoration.LineThrough
    } else {
        TextDecoration.None
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(0.8f),
            text = todo.title,
            textDecoration = textDecoration,
        )
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { isChecked ->
                onEvent(TodoListEvent.OnDoneChange(todo, isChecked))
            },
        )
        IconButton(
            onClick = { onEvent(TodoListEvent.OnDeleteToDo(todo)) },
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "delete Todo",
            )
        }
    }
}
