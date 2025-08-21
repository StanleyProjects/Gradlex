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
    if (report.exists()) {
        check(report.isFile) { "report is not a file" }
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

fun File.file(): File {
    check(isFile) { "Location \"$absolutePath\" is not a file!" }
    return this
}

fun File.assemble(text: String) {
    require(text.isNotEmpty())
    if (exists()) {
        file()
        check(delete()) { "Failed to delete \"$absolutePath\" file!" }
    } else {
        parentFile?.mkdirs() ?: error("File \"$name\" has no parent!")
    }
    writeText(text)
}

fun RegularFile.assemble(text: String): File {
    val file = asFile
    file.assemble(text)
    return file
}

fun File.eff(): File {
    if (!exists()) error("Location \"$absolutePath\" does not exist!")
    if (!isFile) error("Location \"$absolutePath\" is not a file!")
    if (length() == 0L) error("File \"$absolutePath\" is empty!")
    return this
}
