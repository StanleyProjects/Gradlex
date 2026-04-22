package sp.kx.gradlex

import groovy.namespace.QName
import groovy.util.Node
import groovy.xml.XmlParser
import org.gradle.api.file.Directory

/**
 * Parses the content of the given file as XML turning it into a tree
 * of [Node]s.
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val node = project.layout.projectDirectory.xml("foo.xml")
 * node.getAt(QName.valueOf("bar")).forEach {
 *     ...
 * }
 * ```
 * @return the root [Node] of the parsed tree of [Node]s
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun Directory.xml(path: String): Node {
    return XmlParser().parse(file(path).asFile)
}

/**
 * Usage:
 * ```
 * val project: Project = ...
 * val node = project.layout.projectDirectory.xml("foo.xml")
 * node.getAt("bar".qn()).forEach {
 *     ...
 * }
 * ```
 * @return [QName] corresponding to the [this] receiver
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun String.qn(): QName {
    return QName.valueOf(this)
}

/**
 * Provides lookup of [String] attributes by [key].
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val node = project.layout.projectDirectory.xml("foo.xml")
 * node.getAt("bar".qn()).forEach {
 *     println(it.string("baz".qn()))
 * }
 * ```
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun Node.string(key: QName): String {
    val value = attribute(key) ?: error("Attribute by key \"$key\" does not exist!")
    if (value !is String) error("Attribute by key \"$key\" is not String!")
    return value
}

/**
 * Applying the given [transform]ation to each [Node] in the [Node]s by [name].
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val node = project.layout.projectDirectory.xml("foo.xml")
 * val list = node.getAt("bar".qn()).map {
 *     it.string("baz".qn())
 * }
 * ```
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun <T : Any> Node.map(name: QName, transform: (Node) -> T): List<T> {
    val list = getAt(name)
    val size = list.size
    if (size == 0) return emptyList()
    val result = ArrayList<T>(size)
    for (index in 0 until size) {
        val child = list[index]
        if (child !is Node) error("Child $index/${size - 1} is not Node!")
        result.add(transform(child))
    }
    return result
}
