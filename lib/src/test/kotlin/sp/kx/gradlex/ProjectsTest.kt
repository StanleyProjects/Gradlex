package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class ProjectsTest {
    @Test
    fun buildSrcTest() {
        val projectDir = Files.createTempDirectory("ProjectsTest:buildSrcTest").toFile().let {
            FileUtils.canonicalize(it)
        }
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        assertEquals(projectDir.resolve("buildSrc").absolutePath, project.buildSrc.projectDirectory.asFile.absolutePath)
        assertEquals(projectDir.resolve("buildSrc").resolve("build").absolutePath, project.buildSrc.buildDirectory.get().asFile.absolutePath)
    }
}
