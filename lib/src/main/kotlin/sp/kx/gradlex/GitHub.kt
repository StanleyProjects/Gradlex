package sp.kx.gradlex

import org.gradle.api.file.RegularFile
import java.io.File
import java.net.URI
import java.util.Objects

object GitHub {
    class Repository(val owner: String, val name: String) {
        init {
            require(owner.isNotBlank()) { "The owner is blank!" }
            require(name.isNotBlank()) { "The name is blank!" }
        }

        fun uri(): URI {
            return URI("https://github.com/$owner/$name")
        }

        fun pages(): URI {
            return URI("https://$owner.github.io/$name")
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
