package com.arkivanov.mvikotlin.sample.todo.android.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.instanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.sample.todo.android.FrameworkType
import com.arkivanov.mvikotlin.sample.todo.android.LifecycledConsumer
import com.arkivanov.mvikotlin.sample.todo.android.R
import com.arkivanov.mvikotlin.sample.todo.common.controller.TodoListController
import com.arkivanov.mvikotlin.sample.todo.common.database.TodoDatabase
import com.arkivanov.mvikotlin.sample.todo.coroutines.controller.TodoListCoroutinesController
import com.arkivanov.mvikotlin.sample.todo.reaktive.controller.TodoListReaktiveController

class TodoListFragment(
    private val dependencies: Dependencies
) : Fragment(R.layout.todo_list), LifecycledConsumer<TodoListController.Input> {

    private lateinit var controller: TodoListController
    override val input: (TodoListController.Input) -> Unit get() = controller.input

    private fun createController(lifecycle: Lifecycle): TodoListController {
        val todoListControllerDependencies =
            object : TodoListController.Dependencies, Dependencies by dependencies {
                override val instanceKeeper: InstanceKeeper = instanceKeeper()
                override val lifecycle: Lifecycle = lifecycle
            }

        return when (dependencies.frameworkType) {
            FrameworkType.REAKTIVE -> TodoListReaktiveController(todoListControllerDependencies)
            FrameworkType.COROUTINES -> TodoListCoroutinesController(todoListControllerDependencies)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = createController(essentyLifecycle())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller.onViewCreated(TodoListViewImpl(view), TodoAddViewImpl(view), viewLifecycleOwner.essentyLifecycle())
    }

    interface Dependencies {
        val storeFactory: StoreFactory
        val database: TodoDatabase
        val frameworkType: FrameworkType
        val listOutput: (TodoListController.Output) -> Unit
    }
}
