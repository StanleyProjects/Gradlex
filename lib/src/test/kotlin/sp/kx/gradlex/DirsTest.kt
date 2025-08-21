package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class DirsTest {
    @Test
    fun asFileTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        check(projectDir.resolve("foo").mkdirs())
        val expected = "foobarbaz"
        projectDir.resolve("foo/bar").writeText(expected)
        val dir = project.layout.projectDirectory.dir("foo")
        assertEquals(expected, dir.asFile("bar").readText())
    }

    @Test
    fun effTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        check(projectDir.resolve("foo").mkdirs())
        val expected = "foobarbaz"
        projectDir.resolve("foo/bar").writeText(expected)
        val dir = project.layout.projectDirectory.dir("foo")
        assertEquals(expected, dir.eff("bar").readText())
    }
}
