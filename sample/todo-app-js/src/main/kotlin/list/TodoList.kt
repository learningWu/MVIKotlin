package list

import com.arkivanov.mvikotlin.sample.todo.common.database.TodoItem
import com.ccfraser.muirwik.components.button.mIconButton
import com.ccfraser.muirwik.components.list.mList
import com.ccfraser.muirwik.components.list.mListItem
import com.ccfraser.muirwik.components.list.mListItemText
import com.ccfraser.muirwik.components.mCheckbox
import com.ccfraser.muirwik.components.mTypography
import react.RBuilder
import react.buildElement

fun RBuilder.listTodo(
    todos: List<TodoItem>,
    onClick: (String) -> Unit,
    onDoneClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    mList {
        todos.forEach { todoItem ->
            val id = todoItem.id

            mListItem {
                mCheckbox(
                    checked = todoItem.data.isDone,
                    onChange = { _, _ -> onDoneClick(id) }
                )
                mListItemText(
                    primary = buildElement { mTypography(text = todoItem.data.text, noWrap = true) }
                ) {
                    attrs.onClick = { onClick(id) }
                }
                mIconButton(
                    iconName = "delete",
                    onClick = { onDeleteClick(id) }
                )
            }
        }
    }
}
