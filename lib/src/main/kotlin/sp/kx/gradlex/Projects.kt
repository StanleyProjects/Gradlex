package sp.kx.gradlex

import org.gradle.api.Project
import org.gradle.api.file.ProjectLayout

val Project.buildSrc: ProjectLayout get() {
    return FinalProjectLayout(
        objectFactory = rootProject.objects,
        projectDirectory = rootProject.layout.projectDirectory.dir("buildSrc"),
    )
}
