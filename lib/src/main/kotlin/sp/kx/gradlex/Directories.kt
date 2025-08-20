package sp.kx.gradlex

import org.gradle.api.file.Directory
import java.io.File

fun Directory.asFile(path: String): File {
    return file(path).asFile
}
