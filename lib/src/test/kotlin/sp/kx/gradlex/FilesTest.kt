package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
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
    fun fileErrorTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val file = project.layout.projectDirectory.file("foo").asFile
        file.mkdirs()
        assertThrows(IllegalStateException::class.java) {
            file.file()
        }
    }

    @Test
    fun assembleTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val file = project.layout.projectDirectory.dir("foo").file("bar").asFile
        val parentFile = file.parentFile ?: TODO()
        check(!parentFile.exists())
        check(!file.exists())
        val expected = "foobarbaz"
        file.assemble(text = expected)
        assertEquals(expected, file.readText())
        check(file.delete())
        check(parentFile.exists())
        check(!file.exists())
        file.assemble(text = expected)
        assertEquals(expected, file.readText())
        file.writeText("")
        check(parentFile.exists())
        check(file.exists())
        check(file.length() == 0L)
        file.assemble(text = expected)
        assertEquals(expected, file.readText())
    }

    @Test
    fun assembleErrorTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val dir = project.layout.projectDirectory
        val file = dir.dir("foo").file("bar").asFile
        assertThrows(IllegalArgumentException::class.java) {
            file.assemble(text = "")
        }
        val parentFile = file.parentFile ?: TODO()
        check(!file.exists())
        check(file.mkdirs())
        check(parentFile.exists())
        check(file.exists())
        check(!file.isFile)
        assertThrows(IllegalStateException::class.java) {
            file.assemble(text = "baz")
        }
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
    fun effNotExistsTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val issuer = projectDir.resolve("foo")
        check(!issuer.exists())
        assertThrows(IllegalStateException::class.java) {
            issuer.eff()
        }
    }

    @Test
    fun effNotFileTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val issuer = projectDir.resolve("foo")
        check(issuer.mkdirs())
        check(issuer.exists())
        check(!issuer.isFile)
        assertThrows(IllegalStateException::class.java) {
            issuer.eff()
        }
    }

    @Test
    fun effEmptyTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val issuer = projectDir.resolve("foo")
        issuer.writeText("")
        check(issuer.exists())
        check(issuer.isFile)
        assertThrows(IllegalStateException::class.java) {
            issuer.eff()
        }
    }

    @Test
    fun checkTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val dir = project.layout.projectDirectory
        val expected = "foobarbaz\n1234567890"
        dir.file("foo").assemble(expected).check(
            expected = setOf("foobarbaz"),
            regexes = setOf("\\d+".toRegex()),
            report = dir.file("report").asFile,
        )
    }

    @Test
    fun checkErrorTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val dir = project.layout.projectDirectory
        val report = dir.file("report").asFile
        val file = dir.file("foo").asFile
        file.delete()
        assertThrows(IllegalStateException::class.java) {
            file.check(
                expected = emptySet(),
                report = report,
            )
        }
        file.delete()
        file.mkdirs()
        assertThrows(IllegalStateException::class.java) {
            file.check(
                expected = emptySet(),
                report = report,
            )
        }
        file.delete()
        file.writeText("")
        assertThrows(IllegalStateException::class.java) {
            file.check(
                expected = emptySet(),
                report = report,
            )
        }
        file.delete()
        file.writeText("foo")
        assertThrows(IllegalStateException::class.java) {
            file.check(
                expected = setOf("bar"),
                report = report,
            )
        }
        file.delete()
        file.writeText("foo")
        assertThrows(IllegalStateException::class.java) {
            file.check(
                expected = emptySet(),
                regexes = setOf("\\d".toRegex()),
                report = report,
            )
        }
    }
}
