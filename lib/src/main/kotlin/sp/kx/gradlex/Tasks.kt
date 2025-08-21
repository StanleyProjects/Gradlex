package sp.kx.gradlex

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

inline fun <reified T : Task> TaskContainer.get(
    nameSegment: String,
    secondNameSegment: String,
    vararg otherNameSegments: String,
    noinline block: T.() -> Unit,
): T {
    val name = camelCase(camelCase(nameSegment, secondNameSegment), *otherNameSegments)
    val task = getByName(name)
    check(task is T)
    task.block()
    return task
}

inline fun <reified T : Task> TaskContainer.add(
    nameSegment: String,
    secondNameSegment: String,
    vararg otherNameSegments: String,
    noinline block: T.() -> Unit,
): T {
    val name = camelCase(camelCase(nameSegment, secondNameSegment), *otherNameSegments)
    return create(name, T::class.java, block)
}
