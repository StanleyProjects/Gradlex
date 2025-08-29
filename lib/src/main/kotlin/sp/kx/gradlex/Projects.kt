package sp.kx.gradlex

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout

fun Project.buildDir(): Directory {
    return layout.buildDirectory.get()
}

/**
 * Provides access to various important directories for "buildSrc" directory of [Project].
 *
 * Usage:
 * ```
 * val project: Project = ...
 * val file = project.buildSrc.projectDirectory.asFile
 * if (file.exists()) {
 *     assertTrue(file.isDirectory)
 *     assertEquals("buildSrc", file.name)
 * }
 * ```
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
val Project.buildSrc: ProjectLayout get() {
    return FinalProjectLayout(
        objectFactory = rootProject.objects,
        projectDirectory = rootProject.layout.projectDirectory.dir("buildSrc"),
    )
}

fun ProjectLayout.buildDir(): Directory {
    return buildDirectory.get()
}

fun ProjectLayout.dir(path: String): Directory {
    return projectDirectory.dir(path)
}
