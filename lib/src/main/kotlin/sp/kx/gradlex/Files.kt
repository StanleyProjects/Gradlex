package sp.kx.gradlex

import org.gradle.api.file.RegularFile
import java.io.File

private fun checkLines(actual: List<String>, expected: Set<String>): Set<String> {
    return expected.mapNotNull { line ->
        if (actual.none { it.contains(line) }) {
            "the file does not contain \"$line\" line"
        } else {
            null
        }
    }.toSet()
}

private fun checkRegexes(actual: List<String>, regexes: Set<Regex>): Set<String> {
    return regexes.mapNotNull { regex ->
        if (actual.none(regex::containsMatchIn)) {
            "the file does not match \"$regex\" regex"
        } else {
            null
        }
    }.toSet()
}

/**
 * Usage:
 * ```
 * File("/tmp/bar").check(
 *     expected = setOf("foo", "bar"),
 *     report = File("/tmp/report"),
 *     expected = setOf("^f\\w\\d".toRegex()),
 * )
 * ```
 * @receiver The [File] whose contents will be checked.
 * @param expected The set of strings expected to be in [this] receiver [File].
 * @param report Where the test result will be written.
 * @param regexes The set of [Regex] that will match the lines in [this] receiver [File].
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun File.check(
    expected: Set<String>,
    report: File,
    regexes: Set<Regex> = emptySet(),
) {
    val issues = when {
        !exists() -> setOf("the issuer \"$absolutePath\" does not exist")
        !isFile -> setOf("the issuer \"$absolutePath\" is not a file")
        else -> {
            val actual = readLines(Charsets.UTF_8)
            if (actual.isEmpty()) {
                setOf("the file does not contain text")
            } else {
                checkLines(actual = actual, expected = expected) + checkRegexes(actual = actual, regexes = regexes)
            }
        }
    }
    if (report.exists() && report.isFile) {
        check(report.delete())
    } else {
        report.parentFile?.mkdirs() ?: error("report has no parent")
    }
    if (issues.isEmpty()) {
        val message = "All checks of the file along the \"$name\" were successful."
        report.writeText(message)
        println(message)
        return
    }
    val text = """
        <html>
        <h3>The following problems were found while checking the <code>$name</code>:</h3>
        ${issues.joinToString(prefix = "<ul>", postfix = "</ul>", separator = "\n") { "<li>$it</li>" }}
        </html>
    """.trimIndent()
    report.writeText(text)
    error("Problems were found while checking the \"$name\". See the report ${report.absolutePath}")
}

/**
 * Usage:
 * ```
 * val file = File("/tmp/bar").file()
 * assertTrue(file.isFile)
 * ```
 * @return [this] receiver [File].
 * @throws IllegalStateException if [this] receiver [File] is not a normal file.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun File.file(): File {
    check(isFile) { "Location \"$absolutePath\" is not a file!" }
    return this
}


/**
 * Usage:
 * ```
 * val text = "foo"
 * val file = File("/tmp/bar").assemble(text)
 * assertEquals(text, file.readText())
 * ```
 * @receiver The [File] to which the [text] will be written.
 * @throws IllegalArgumentException if [text] is empty.
 * @throws IllegalStateException if [this] receiver [File] exists and not a file.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun File.assemble(text: String) {
    require(text.isNotEmpty())
    if (exists()) {
        if (isFile) {
            check(delete()) { "Failed to delete \"$absolutePath\" file!" }
        } else {
            error("Location \"$absolutePath\" is not a file!")
        }
    } else {
        val parentFile = parentFile ?: error("File \"$name\" has no parent!")
        if (!parentFile.exists() || !parentFile.isDirectory) {
            check(parentFile.mkdirs())
        }
    }
    writeText(text)
}

/**
 * Usage:
 * ```
 * val text = "foo"
 * val file = layout.buildDirectory.get()
 *     .dir("bar")
 *     .file("baz")
 *     .assemble(text)
 * assertEquals(text, file.readText())
 * ```
 * @receiver The [RegularFile] to which the [text] will be written.
 * @return The [File] of [this] receiver [RegularFile].
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun RegularFile.assemble(text: String): File {
    val file = asFile
    file.assemble(text)
    return file
}

/**
 * Usage:
 * ```
 * val file = File("/tmp/bar").eff()
 * assertTrue(file.exists())
 * assertTrue(file.isFile)
 * assertTrue(file.length() > 0)
 * ```
 * @return [this] receiver file.
 * @throws IllegalStateException if [this] receiver file does not exist.
 * @throws IllegalStateException if [this] receiver file is not a normal file.
 * @throws IllegalStateException if [this] receiver file is empty.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun File.eff(): File {
    check(exists()) { "Location \"$absolutePath\" does not exist!" }
    check(isFile) { "Location \"$absolutePath\" is not a file!" }
    check(length() > 0) { "File \"$absolutePath\" is empty!" }
    return this
}
