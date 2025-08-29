package sp.kx.gradlex

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

/**
 * Locates a [Task] by name segments in camel case, failing if there is no such object.
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val task = project.tasks.get("foo", "bar") {
 *     ...
 * }
 * assertEquals("fooBar", task.name)
 * ```
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 * @see TaskContainer.getByName
 */
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

/**
 * Creates a [Task] with the given name segments in camel case,
 * configures it with the given [block], and adds it to [this].
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val task = project.tasks.create("foo", "bar") {
 *     ...
 * }
 * assertEquals("fooBar", task.name)
 * ```
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 * @see TaskContainer.create
 */
fun TaskContainer.create(
    name0: String,
    name1: String,
    vararg nameN: String,
    block: Task.() -> Unit,
): Task {
    val name = camelCase(name0, name1, *nameN)
    return create(name, block)
}

/**
 * Creates a [Task] with the given name segments in camel case and [T] type,
 * configures it with the given [block], and adds it to [this].
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val task = project.tasks.add<DefaultTask>("foo", "bar") {
 *     ...
 * }
 * assertEquals("fooBar", task.name)
 * ```
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 * @see TaskContainer.create
 */
inline fun <reified T : Task> TaskContainer.add(
    name0: String,
    name1: String,
    vararg nameN: String,
    noinline block: T.() -> Unit,
): T {
    val name = camelCase(name0, name1, *nameN)
    return create(name, T::class.java, block)
}
