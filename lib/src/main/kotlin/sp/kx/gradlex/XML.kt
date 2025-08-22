package sp.kx.gradlex

import groovy.namespace.QName
import groovy.util.Node
import groovy.xml.XmlParser
import org.gradle.api.file.Directory

fun Directory.xml(path: String): Node {
    return XmlParser().parse(file(path).asFile)
}

fun String.qn(): QName {
    return QName.valueOf(this)
}

fun Node.string(key: QName): String {
    val value = attribute(key) ?: error("Attribute by key \"$key\" does not exist!")
    if (value !is String) error("Attribute by key \"$key\" is not String!")
    return value
}

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
