package sp.kx.gradlex

import org.gradle.api.file.RegularFile
import java.io.File
import java.net.URI
import java.util.Objects

/**
 * A set of functions for working with [GitHub](https://github.com).
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
object GitHub {
    /**
     * Encapsulates data about a GitHub [repository](https://docs.github.com/en/rest/repos).
     * @property owner The account owner of the GitHub repository.
     * @property name The name of the GitHub repository.
     * @throws IllegalArgumentException if [owner] is blank.
     * @throws IllegalArgumentException if [name] is blank.
     * @author [Stanley Wintergreen](https://github.com/kepocnhh)
     * @since 0.1.0
     */
    class Repository(val owner: String, val name: String) {
        init {
            require(owner.isNotBlank()) { "The owner is blank!" }
            require(name.isNotBlank()) { "The name is blank!" }
        }

        /**
         * Usage:
         * ```
         * val repository = GitHub.Repository(owner = "foo", name = "bar")
         * val expected = URI("https://github.com/foo/bar")
         * assertEquals(expected, repository.uri())
         * ```
         * @return The [URI] of the root of this [GitHub.Repository].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun uri(): URI {
            return URI("https://github.com/$owner/$name")
        }

        /**
         * Usage:
         * ```
         * val repository = GitHub.Repository(owner = "foo", name = "bar")
         * val expected = URI("https://github.com/foo/bar/baz")
         * assertEquals(expected, repository.uri(path = "baz"))
         * ```
         * @return The [URI] of the [path] of this [GitHub.Repository].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun uri(path: String): URI {
            val builder = StringBuilder("https://github.com/$owner/$name")
            val split = path.split("/")
            for (it in split) {
                if (it.isNotBlank()) builder.append("/").append(it)
            }
            return URI(builder.toString())
        }

        /**
         * Usage:
         * ```
         * val repository = GitHub.Repository(owner = "foo", name = "bar")
         * val expected = URI("https://foo.github.io/bar")
         * assertEquals(expected, repository.pages())
         * ```
         * @return The [URI] of the root of the GitHub [pages](https://pages.github.com) of this [GitHub.Repository].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun pages(): URI {
            return URI("https://$owner.github.io/$name")
        }

        /**
         * Usage:
         * ```
         * val repository = GitHub.Repository(owner = "foo", name = "bar")
         * val expected = URI("https://foo.github.io/bar/baz")
         * assertEquals(expected, repository.pages(path = "baz"))
         * ```
         * @return The [URI] of the [path] of the GitHub [pages](https://pages.github.com) of this [GitHub.Repository].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun pages(path: String): URI {
            val builder = StringBuilder("https://$owner.github.io/$name")
            val split = path.split("/")
            for (it in split) {
                if (it.isNotBlank()) builder.append("/").append(it)
            }
            return URI(builder.toString())
        }

        /**
         * Usage:
         * ```
         * val repository = GitHub.Repository(owner = "foo", name = "bar")
         * val expected = URI("https://github.com/foo/bar/releases/tag/1.2.3")
         * assertEquals(expected, repository.release(version = "1.2.3"))
         * ```
         * @throws IllegalArgumentException if [version] is blank.
         * @return The [URI] of the release of this [GitHub.Repository].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun release(version: String): URI {
            require(version.isNotBlank()) { "The version is blank!" }
            return URI("https://github.com/$owner/$name/releases/tag/$version")
        }

        /**
         * Writes data about this [GitHub.Repository] to a [target].
         *
         * Usage:
         * ```
         * val project: Project = ...
         * val gh = GitHub.Repository(owner = "foo", name = "bar")
         * val target = project.layout.projectDirectory.file("metadata.yml")
         * val file = gh.assemble(version = "1.2.3", target = target)
         * assertTrue(file.exists())
         * assertEquals("metadata.yml", file.name)
         * ```
         * @throws IllegalArgumentException if [version] is blank.
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun assemble(version: String, target: RegularFile): File {
            require(version.isNotBlank()) { "The version is blank!" }
            val text = """
                repository:
                 owner: '$owner'
                 name: '$name'
                version: '$version'
            """.trimIndent()
            return target.assemble(text)
        }

        override fun toString(): String {
            return "Repository($owner/$name)"
        }

        override fun hashCode(): Int {
            return Objects.hash(owner, name)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Repository) return false
            return owner == other.owner && name == other.name
        }
    }

    fun pages(owner: String): URI {
        require(owner.isNotBlank()) { "The owner is blank!" }
        return URI("https://$owner.github.io")
    }

    fun pages(owner: String, path: String): URI {
        require(owner.isNotBlank()) { "The owner is blank!" }
        val builder = StringBuilder("https://$owner.github.io")
        val split = path.split("/")
        for (it in split) {
            if (it.isNotBlank()) builder.append("/").append(it)
        }
        return URI(builder.toString())
    }
}
