package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Files
import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import java.util.concurrent.atomic.AtomicInteger
import org.gradle.api.DefaultTask

internal class TasksTest {
    @Test
    fun getTest() {
        val projectDir = Files.createTempDirectory("TasksTest:getTest").toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val counter = AtomicInteger(0)
        assertEquals(0, counter.get())
        val expected = "fooBarBazQux"
        project.tasks.create(expected, DefaultTask::class.java) {
            counter.incrementAndGet()
        }
        val task = project.tasks.get<DefaultTask>("foo", "bar", "", "baz", " ", "qux") {
            counter.incrementAndGet()
        }
        assertEquals(expected, task.name)
        assertEquals(2, counter.get())
    }

    @Test
    fun addTest() {
        val projectDir = Files.createTempDirectory("TasksTest:addTest").toFile().let(FileUtils::canonicalize)
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
