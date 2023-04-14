package com.gulderbone.todolist.screens.todolist

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gulderbone.todolist.data.Todo

@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (TodoListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (todo.isDone) {
        MaterialTheme.colorScheme.inverseSurface
    } else {
        MaterialTheme.colorScheme.surface
    }
    Row(
        modifier = modifier
            .background(background),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(0.8f),
            text = todo.title,
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
