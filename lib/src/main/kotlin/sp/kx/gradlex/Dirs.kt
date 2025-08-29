package sp.kx.gradlex

import org.gradle.api.file.Directory
import java.io.File

/**
 * Usage:
 * ```
 * val project: Project = ...
 * val file = project.layout.projectDirectory.dir("foo").asFile("bar")
 * assertTrue(file.exists())
 * ```
 * @return a [File] whose value is a [Directory] whose location is the given path resolved relative to this directory.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 * @see [Directory.file]
 */
fun Directory.asFile(path: String): File {
    return file(path).asFile
}

/**
 * Usage:
 * ```
 * val project: Project = ...
 * val file = project.layout.projectDirectory.eff("foo")
 * assertTrue(file.exists())
 * assertTrue(file.isFile)
 * assertTrue(file.length() > 0)
 * ```
 * @return a [File] whose value is a [Directory] whose location is the given path resolved relative to this directory.
 * @throws IllegalStateException if target [File] does not exist.
 * @throws IllegalStateException if target [File] is not a normal file.
 * @throws IllegalStateException if target [File] is empty.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun Directory.eff(path: String): File {
    val file = file(path).asFile
    check(file.exists()) { "Location \"${file.absolutePath}\" does not exist!" }
    check(file.isFile) { "Location \"${file.absolutePath}\" is not a file!" }
    check(file.length() > 0) { "File \"${file.absolutePath}\" is empty!" }
    return file
}
