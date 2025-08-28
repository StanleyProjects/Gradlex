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

        fun uri(): URI {
            return URI("https://github.com/$owner/$name")
        }

        fun uri(path: String): URI {
            val builder = StringBuilder("https://github.com/$owner/$name")
            val split = path.split("/")
            for (it in split) {
                if (it.isNotBlank()) builder.append("/").append(it)
            }
            return URI(builder.toString())
        }

        fun pages(): URI {
            return URI("https://$owner.github.io/$name")
        }

        fun pages(path: String): URI {
            val builder = StringBuilder("https://$owner.github.io/$name")
            val split = path.split("/")
            for (it in split) {
                if (it.isNotBlank()) builder.append("/").append(it)
            }
            return URI(builder.toString())
        }

        fun release(version: String): URI {
            require(version.isNotBlank()) { "The version is blank!" }
            return URI("https://github.com/$owner/$name/releases/tag/$version")
        }

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
}
