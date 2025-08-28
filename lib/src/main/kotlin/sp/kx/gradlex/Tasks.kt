package sp.kx.gradlex

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

inline fun <reified T : Task> TaskContainer.get(
    name0: String,
    name1: String,
    vararg nameN: String,
): T {
    val name = camelCase(name0, name1, *nameN)
    val task = getByName(name)
    check(task is T)
    return task
}

fun TaskContainer.create(
    name0: String,
    name1: String,
    vararg nameN: String,
    block: Task.() -> Unit,
): Task {
    val name = camelCase(name0, name1, *nameN)
    return create(name, block)
}

inline fun <reified T : Task> TaskContainer.add(
    name0: String,
    name1: String,
    vararg nameN: String,
    noinline block: T.() -> Unit,
): T {
    val name = camelCase(name0, name1, *nameN)
    return create(name, T::class.java, block)
}
