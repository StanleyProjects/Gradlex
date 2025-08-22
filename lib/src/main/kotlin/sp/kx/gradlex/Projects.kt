package sp.kx.gradlex

import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout

fun Project.buildDir(): Directory {
    return layout.buildDirectory.get()
}

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
