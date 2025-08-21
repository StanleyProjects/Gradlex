package sp.kx.gradlex

import org.gradle.api.file.Directory
import java.io.File

fun Directory.asFile(path: String): File {
    return file(path).asFile
}

fun Directory.eff(path: String): File {
    val file = file(path).asFile
    if (!file.exists()) error("Location \"${file.absolutePath}\" does not exist!")
    if (!file.isFile) error("Location \"${file.absolutePath}\" is not a file!")
    if (file.length() == 0L) error("File \"${file.absolutePath}\" is empty!")
    return file
}
