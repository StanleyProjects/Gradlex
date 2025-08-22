package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.net.URI
import java.nio.file.Files
import java.util.Objects

internal class GitHubTest {
    @Test
    fun constructorErrorTest() {
        assertThrows(IllegalArgumentException::class.java) {
            GitHub.Repository(owner = "", name = "")
        }
        assertThrows(IllegalArgumentException::class.java) {
            GitHub.Repository(owner = "1", name = "")
        }
    }

    @Test
    fun uriTest() {
        val owner = "foo"
        val name = "bar"
        val gh = GitHub.Repository(owner = owner, name = name)
        val expected = URI("https://github.com/$owner/$name")
        val actual = gh.uri()
        assertEquals(expected, actual)
    }

    @Test
    fun pagesTest() {
        val owner = "foo"
        val name = "bar"
        val gh = GitHub.Repository(owner = owner, name = name)
        val expected = URI("https://$owner.github.io/$name")
        val actual = gh.pages()
        assertEquals(expected, actual)
    }

    @Test
    fun releaseTest() {
        val owner = "foo"
        val name = "bar"
        val gh = GitHub.Repository(owner = owner, name = name)
        val version = "baz"
        val expected = URI("https://github.com/$owner/$name/releases/tag/$version")
        val actual = gh.release(version = version)
        assertEquals(expected, actual)
    }

    @Test
    fun releaseErrorTest() {
        val owner = "foo"
        val name = "bar"
        val gh = GitHub.Repository(owner = owner, name = name)
        val version = ""
        assertThrows(IllegalArgumentException::class.java) {
            gh.release(version = version)
        }
    }

    @Test
    fun assembleTest() {
        val owner = "foo"
        val name = "bar"
        val gh = GitHub.Repository(owner = owner, name = name)
        val version = "baz"
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val target = project.layout.projectDirectory.file("foo")
        val expected = """
            repository:
             owner: '$owner'
             name: '$name'
            version: '$version'
        """.trimIndent()
        val actual = gh.assemble(version = version, target = target).readText()
        assertEquals(expected, actual)
    }

    @Test
    fun assembleErrorTest() {
        val owner = "foo"
        val name = "bar"
        val issuer = GitHub.Repository(owner = owner, name = name)
        val version = ""
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val target = project.layout.projectDirectory.file("foo")
        assertThrows(IllegalArgumentException::class.java) {
            issuer.assemble(version = version, target = target)
        }
    }

    @Test
    fun toStringTest() {
        val owner = "foo"
        val name = "bar"
        val issuer = GitHub.Repository(owner = owner, name = name)
        val expected = "Repository($owner/$name)"
        val actual = issuer.toString()
        assertEquals(expected, actual)
    }

    @Test
    fun hashCodeTest() {
        val owner = "foo"
        val name = "bar"
        val issuer = GitHub.Repository(owner = owner, name = name)
        val expected = Objects.hash(owner, name)
        val actual = issuer.hashCode()
        assertEquals(expected, actual)
    }

    @Test
    fun equalsTest() {
        val owner = "foo"
        val name = "bar"
        val i0 = GitHub.Repository(owner = owner, name = name)
        val i1 = GitHub.Repository(owner = owner, name = name)
        val i2 = GitHub.Repository(owner = name, name = owner)
        val i3 = GitHub.Repository(owner = owner, name = owner)
        assertEquals(true, i0 == i1)
        assertEquals(false, i0 == i2)
        assertEquals(false, i0 == i3)
        assertEquals(false, i0.equals(Unit))
    }
}
