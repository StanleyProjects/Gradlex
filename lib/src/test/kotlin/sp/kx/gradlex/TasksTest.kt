package sp.kx.gradlex

import org.gradle.api.DefaultTask
import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger

internal class TasksTest {
    @Test
    fun getTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val counter = AtomicInteger(0)
        assertEquals(0, counter.get())
        val expected = "fooBarBazQux"
        project.tasks.create(expected, DefaultTask::class.java) {
            counter.incrementAndGet()
        }
        val task = project.tasks.get<DefaultTask>("foo", "bar", "", "baz", " ", "qux")
        assertEquals(expected, task.name)
        assertEquals(1, counter.get())
    }

    @Test
    fun createTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val counter = AtomicInteger(0)
        assertEquals(0, counter.get())
        val task = project.tasks.create("foo", "bar", "", "baz", " ", "qux") {
            counter.incrementAndGet()
        }
        val expected = "fooBarBazQux"
        assertEquals(expected, task.name)
        assertEquals(1, counter.get())
    }

    @Test
    fun addTest() {
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val counter = AtomicInteger(0)
        assertEquals(0, counter.get())
        val task = project.tasks.add<DefaultTask>("foo", "bar", "", "baz", " ", "qux") {
            counter.incrementAndGet()
        }
        val expected = "fooBarBazQux"
        assertEquals(expected, task.name)
        assertEquals(1, counter.get())
    }
}
