package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
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

    @Test
    fun effNotExistsTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val name = "foo"
        val issuer = projectDir.resolve(name)
        check(!issuer.exists())
        assertThrows(IllegalStateException::class.java) {
            project.layout.projectDirectory.eff(name)
        }
    }

    @Test
    fun effNotFileTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val name = "foo"
        val issuer = projectDir.resolve(name)
        check(issuer.mkdirs())
        check(issuer.exists())
        check(!issuer.isFile)
        assertThrows(IllegalStateException::class.java) {
            project.layout.projectDirectory.eff(name)
        }
    }

    @Test
    fun effEmptyTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val name = "foo"
        val issuer = projectDir.resolve(name)
        issuer.writeText("")
        check(issuer.exists())
        check(issuer.isFile)
        assertThrows(IllegalStateException::class.java) {
            project.layout.projectDirectory.eff(name)
        }
    }
}
