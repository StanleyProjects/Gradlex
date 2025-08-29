package sp.kx.gradlex

import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import java.io.File

internal class FinalProjectLayout(
    private val objectFactory: ObjectFactory,
    private val projectDirectory: Directory,
) : ProjectLayout {
    override fun getProjectDirectory(): Directory {
        return projectDirectory
    }

    override fun getBuildDirectory(): DirectoryProperty {
        return objectFactory.directoryProperty().value(projectDirectory.dir("build"))
    }

    override fun file(file: Provider<File>): Provider<RegularFile> {
        return projectDirectory.file(file.map { it.path })
    }

    override fun dir(file: Provider<File>): Provider<Directory> {
        return projectDirectory.dir(file.map { it.path })
    }

    override fun files(vararg paths: Any?): FileCollection {
        return projectDirectory.files(*paths)
    }
}
