package sp.kx.gradlex

import org.gradle.api.file.RegularFile
import java.io.File
import java.net.URI
import java.util.Objects

/**
 * A set of functions and types for working with [Maven](https://maven.apache.org).
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
object Maven {
    /**
     * Maven central repository.
     * @author [Stanley Wintergreen](https://github.com/kepocnhh)
     * @since 0.1.0
     */
    val Host = URI("https://central.sonatype.com")

    /**
     * Encapsulates data about an [artifact](https://maven.apache.org/repositories/artifacts.html).
     * @property group The artifact group.
     * @property id The artifact id.
     * @throws IllegalArgumentException if [group] is blank.
     * @throws IllegalArgumentException if [id] is blank.
     * @author [Stanley Wintergreen](https://github.com/kepocnhh)
     * @since 0.1.0
     */
    @Suppress("TooManyFunctions")
    class Artifact(val group: String, val id: String) {
        init {
            require(group.isNotBlank()) { "The group ID is blank!" }
            require(id.isNotBlank()) { "The artifact ID is blank!" }
        }

        /**
         * Usage:
         * ```
         * val artifact = Maven.Artifact(group = "foo", id = "bar")
         * assertEquals("bar-1.2.3", artifact.name(version = "1.2.3"))
         * ```
         * @throws IllegalArgumentException if [version] is blank.
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun name(version: String, separator: Char = '-'): String {
            require(version.isNotBlank()) { "The version is blank!" }
            return "$id$separator$version"
        }

        fun pom(
            version: String,
            packaging: String,
            modelVersion: String = "4.0.0",
        ): String {
            require(modelVersion.isNotBlank()) { "The model version is blank!" }
            require(version.isNotBlank()) { "The version is blank!" }
            require(packaging.isNotBlank()) { "The packaging is blank!" }
            val host = URI("http://maven.apache.org")
            val url = URI("$host/POM/$modelVersion")
            val project = setOf(
                "xsi:schemaLocation" to "$url $host/xsd/maven-$modelVersion.xsd",
                "xmlns" to url,
                "xmlns:xsi" to "http://www.w3.org/2001/XMLSchema-instance",
            ).joinToString(separator = " ") { (key, value) ->
                "$key=\"$value\""
            }
            return setOf(
                "modelVersion" to modelVersion,
                "groupId" to group,
                "artifactId" to id,
                "version" to version,
                "packaging" to packaging,
            ).joinToString(
                prefix = "<project $project>",
                separator = "",
                postfix = "</project>",
            ) { (key, value) ->
                "<$key>$value</$key>"
            }
        }

        @Suppress("LongParameterList")
        fun pom(
            version: String,
            packaging: String,
            name: String = id,
            description: String = id,
            uri: URI,
            licenses: Set<URI>,
            scm: URI,
            tag: String = version,
            developers: Set<String>,
            modelVersion: String = "4.0.0",
        ): String {
            require(modelVersion.isNotBlank()) { "The model version is blank!" }
            require(version.isNotBlank()) { "The version is blank!" }
            require(packaging.isNotBlank()) { "The packaging is blank!" }
            require(name.isNotBlank()) { "The name is blank!" }
            require(description.isNotBlank()) { "The description is blank!" }
            require(tag.isNotBlank()) { "The tag is blank!" }
            require(licenses.isNotEmpty()) { "No licenses!" }
            require(developers.isNotEmpty()) { "No developers!" }
            require(developers.none { it.isBlank() }) { "Wrong developers!" }
            val host = URI("http://maven.apache.org")
            val url = URI("$host/POM/$modelVersion")
            val project = setOf(
                "xsi:schemaLocation" to "$url $host/xsd/maven-$modelVersion.xsd",
                "xmlns" to url,
                "xmlns:xsi" to "http://www.w3.org/2001/XMLSchema-instance",
            ).joinToString(separator = " ") { (key, value) ->
                "$key=\"$value\""
            }
            return setOf(
                "modelVersion" to modelVersion,
                "groupId" to group,
                "artifactId" to id,
                "version" to version,
                "packaging" to packaging,
                "name" to name,
                "description" to description,
                "url" to uri.toString(),
                "licenses" to licenses.joinToString { "<license><url>$it</url></license>" },
                "scm" to "<tag>$tag</tag><url>$scm</url>",
                "developers" to developers.joinToString { "<developer><name>$it</name></developer>" },
            ).joinToString(
                prefix = "<project $project>",
                separator = "",
                postfix = "</project>",
            ) { (key, value) ->
                "<$key>$value</$key>"
            }
        }

        /**
         * Usage:
         * ```
         * val artifact = Maven.Artifact(group = "foo", id = "bar")
         * assertEquals("foo:bar", artifact.moduleName())
         * ```
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun moduleName(separator: Char = ':'): String {
            return "$group$separator$id"
        }

        /**
         * Usage:
         * ```
         * val artifact = Maven.Artifact(group = "foo", id = "bar")
         * assertEquals("foo:bar:1.2.3", artifact.moduleName(version = "1.2.3"))
         * ```
         * @throws IllegalArgumentException if [version] is blank.
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun moduleName(version: String, separator: Char = ':'): String {
            require(version.isNotBlank()) { "The version is blank!" }
            return "$group$separator$id$separator$version"
        }

        fun assemble(version: String, target: RegularFile): File {
            require(version.isNotBlank()) { "The version is blank!" }
            val text = """
                repository:
                 groupId: '$group'
                 artifactId: '$id'
                version: '$version'
            """.trimIndent()
            return target.assemble(text)
        }

        /**
         * Usage:
         * ```
         * val artifact = Maven.Artifact(group = "foo", id = "bar")
         * val expected = URI("https://central.sonatype.com/artifact/foo/bar")
         * assertEquals(expected, artifact.uri())
         * ```
         * @return The [URI] of the root of this [Maven.Artifact].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun uri(): URI {
            return URI("$Host/artifact/$group/$id")
        }

        /**
         * Usage:
         * ```
         * val artifact = Maven.Artifact(group = "foo", id = "bar")
         * val expected = URI("https://central.sonatype.com/artifact/foo/bar/1.2.3")
         * assertEquals(expected, artifact.uri(version = "1.2.3"))
         * ```
         * @return The [URI] of the [version] of this [Maven.Artifact].
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        fun uri(version: String): URI {
            require(version.isNotBlank()) { "The version is blank!" }
            return URI("$Host/artifact/$group/$id/$version")
        }

        override fun toString(): String {
            return "Artifact($group/$id)"
        }

        override fun hashCode(): Int {
            return Objects.hash(group, id)
        }

        override fun equals(other: Any?): Boolean {
            if (other !is Artifact) return false
            return group == other.group && id == other.id
        }
    }

    /**
     * A set of functions and types for working with Maven snapshot [repositories](https://central.sonatype.com/repository/maven-snapshots).
     * @author [Stanley Wintergreen](https://github.com/kepocnhh)
     * @since 0.1.0
     */
    object Snapshot {
        /**
         * Maven snapshots repository.
         * @author [Stanley Wintergreen](https://github.com/kepocnhh)
         * @since 0.1.0
         */
        val Host = URI("https://central.sonatype.com/repository/maven-snapshots")

        fun metadata(artifact: Artifact): URI {
            return URI("$Host/${artifact.group.replace('.', '/')}/${artifact.id}/maven-metadata.xml")
        }
    }
}
