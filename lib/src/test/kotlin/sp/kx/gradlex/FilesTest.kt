package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class FilesTest {
    @Test
    fun fileTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val expected = "foobarbaz"
        val target = project.layout.projectDirectory.file("foo").assemble(expected)
        val actual = target.file().readText()
        assertEquals(expected, actual)
    }

    @Test
    fun effTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val expected = "foobarbaz"
        val target = project.layout.projectDirectory.file("foo").assemble(expected)
        val actual = target.eff().readText()
        assertEquals(expected, actual)
    }

    @Test
    fun checkTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val dir = project.layout.projectDirectory
        val expected = "foobarbaz"
        dir.file("foo").assemble(expected).check(
            expected = setOf(expected),
            report = dir.file("report").asFile,
        )
    }
}
