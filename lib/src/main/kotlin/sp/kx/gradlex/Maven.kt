package sp.kx.gradlex

import org.gradle.api.file.RegularFile
import java.io.File
import java.net.URI

object Maven {
    class Artifact(val group: String, val id: String) {
        init {
            require(group.isNotBlank()) { "The group ID is blank!" }
            require(id.isNotBlank()) { "The artifact ID is blank!" }
        }

        fun name(version: String, separator: Char = '-'): String {
            require(version.isNotBlank()) { "The version is blank!" }
            return "$id$separator$version"
        }

        fun pom(
            modelVersion: String = "4.0.0",
            version: String,
            packaging: String,
        ): String {
            require(modelVersion.isNotBlank()) { "The model version is blank!" }
            require(version.isNotBlank()) { "The version is blank!" }
            require(packaging.isNotBlank()) { "The packaging is blank!" }
            val host = "http://maven.apache.org"
            val url = "$host/POM/$modelVersion"
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

        fun moduleName(separator: Char = ':'): String {
            return "$group$separator$id"
        }

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
    }

    object Snapshot {
        val Host = URI("https://central.sonatype.com/repository/maven-snapshots")

        fun metadata(
            group: String,
            id: String,
        ): URI {
            require(group.isNotBlank()) { "The group ID is blank!" }
            require(id.isNotBlank()) { "The artifact ID is blank!" }
            return Host.resolve("${group.replace('.', '/')}/$id/maven-metadata.xml")
        }
    }
}
