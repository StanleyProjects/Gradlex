package sp.kx.gradlex

import groovy.namespace.QName
import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class XMLTest {
    @Test
    fun xmlTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        projectDir.resolve("bar").writeText(xml)
        val actual = project.layout.projectDirectory.xml("bar").getAt(QName.valueOf("uses-permission")).map {
            check(it is groovy.util.Node)
            val key = QName.valueOf("{http://schemas.foo.com/foo}name")
            it.attribute(key) as String
        }.toSortedSet()
        assertEquals(expected, actual)
    }

    @Test
    fun qnTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        projectDir.resolve("bar").writeText(xml)
        val actual = project.layout.projectDirectory.xml("bar").getAt("uses-permission".qn()).map {
            check(it is groovy.util.Node)
            val key = "{http://schemas.foo.com/foo}name".qn()
            it.attribute(key) as String
        }.toSortedSet()
        assertEquals(expected, actual)
    }

    @Test
    fun stringTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        projectDir.resolve("bar").writeText(xml)
        val actual = project.layout.projectDirectory.xml("bar").getAt("uses-permission".qn()).map {
            check(it is groovy.util.Node)
            val key = "{http://schemas.foo.com/foo}name".qn()
            it.string(key)
        }.toSortedSet()
        assertEquals(expected, actual)
    }

    @Test
    fun stringNotFoundTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val xml = """
            <manifest xmlns:foo="http://schemas.foo.com/foo">
            <uses-permission foo:foo="abcd_01"/>
            </manifest>
        """.trimIndent()
        projectDir.resolve("bar").writeText(xml)
        project.layout.projectDirectory.xml("bar").getAt("uses-permission".qn()).forEach {
            check(it is groovy.util.Node)
            val key = "{http://schemas.foo.com/foo}name".qn()
            val error = assertThrows(IllegalStateException::class.java) {
                it.string(key)
            }
            assertEquals("Attribute by key \"$key\" does not exist!", error.message)
        }
    }

    @Test
    fun mapTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        projectDir.resolve("bar").writeText(xml)
        val actual = project.layout.projectDirectory.xml("bar").map("uses-permission".qn()) {
            it.string("{http://schemas.foo.com/foo}name".qn())
        }.toSortedSet()
        assertEquals(expected, actual)
    }

    @Test
    fun mapEmptyTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val xml = """
            <manifest xmlns:foo="http://schemas.foo.com/foo">
            </manifest>
        """.trimIndent()
        projectDir.resolve("bar").writeText(xml)
        val actual = project.layout.projectDirectory.xml("bar").map("uses-permission".qn()) {
            it.string("{http://schemas.foo.com/foo}name".qn())
        }.toSortedSet()
        assertEquals(emptySet<String>(), actual)
    }

    companion object {
        private const val xml = """
            <manifest xmlns:foo="http://schemas.foo.com/foo">
            <uses-permission foo:name="abcd_01"/>
            <uses-permission foo:name="abcd_02"/>
            </manifest>
        """
        private val expected = sortedSetOf(
            "abcd_01",
            "abcd_02",
        )
    }
}
