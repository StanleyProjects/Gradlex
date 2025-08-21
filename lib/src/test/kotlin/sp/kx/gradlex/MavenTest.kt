package sp.kx.gradlex

import org.gradle.internal.FileUtils
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.net.URI
import java.nio.file.Files
import java.util.Objects

internal class MavenTest {
    @Test
    fun constructorErrorTest() {
        assertThrows(IllegalArgumentException::class.java) {
            Maven.Artifact(group = "", id = "")
        }
        assertThrows(IllegalArgumentException::class.java) {
            Maven.Artifact(group = "1", id = "")
        }
    }

    @Test
    fun nameTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = "baz"
        val expected = "$id-$version"
        val actual = issuer.name(version = version)
        assertEquals(expected, actual)
    }

    @Test
    fun nameErrorTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = ""
        assertThrows(IllegalArgumentException::class.java) {
            issuer.name(version = version)
        }
    }

    @Test
    fun moduleNameTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val expected = "$group:$id"
        val actual = issuer.moduleName()
        assertEquals(expected, actual)
    }

    @Test
    fun moduleNameVersionTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = "baz"
        val expected = "$group:$id:$version"
        val actual = issuer.moduleName(version = version)
        assertEquals(expected, actual)
    }


    @Test
    fun moduleNameErrorTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = ""
        assertThrows(IllegalArgumentException::class.java) {
            issuer.moduleName(version = version)
        }
    }

    @Test
    fun pomTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = "baz"
        val packaging = "jar"
        val actual = issuer.pom(
            version = version,
            packaging = packaging,
        )
        val modelVersion = "4.0.0"
        val expected = """<project xsi:schemaLocation="http://maven.apache.org/POM/$modelVersion http://maven.apache.org/xsd/maven-$modelVersion.xsd" xmlns="http://maven.apache.org/POM/$modelVersion" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">""" +
            "<modelVersion>$modelVersion</modelVersion>" +
            "<groupId>$group</groupId>" +
            "<artifactId>$id</artifactId>" +
            "<version>$version</version>" +
            "<packaging>$packaging</packaging>" +
            "</project>"
        assertEquals(expected, actual)
    }

    @Test
    fun pomErrorTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "",
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "",
                packaging = "jar",
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                modelVersion = "",
                version = "baz",
                packaging = "jar",
            )
        }
    }

    @Test
    fun pomReleaseTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = "baz"
        val packaging = "jar"
        val uri = URI("https://foo.com")
        val license = URI("https://foo.com/license")
        val scm = URI("https://foo.com/scm")
        val developer = "test developer"
        val actual = issuer.pom(
            version = version,
            packaging = packaging,
            uri = uri,
            licenses = setOf(license),
            scm = scm,
            developers = setOf(developer),
        )
        val modelVersion = "4.0.0"
        val expected = """<project xsi:schemaLocation="http://maven.apache.org/POM/$modelVersion http://maven.apache.org/xsd/maven-$modelVersion.xsd" xmlns="http://maven.apache.org/POM/$modelVersion" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">""" +
            "<modelVersion>$modelVersion</modelVersion>" +
            "<groupId>$group</groupId>" +
            "<artifactId>$id</artifactId>" +
            "<version>$version</version>" +
            "<packaging>$packaging</packaging>" +
            "<name>$id</name>" +
            "<description>$id</description>" +
            "<url>$uri</url>" +
            "<licenses><license><url>$license</url></license></licenses>" +
            "<scm><tag>$version</tag><url>$scm</url></scm>" +
            "<developers><developer><name>$developer</name></developer></developers>" +
            "</project>"
        assertEquals(expected, actual)
    }

    @Test
    fun pomReleaseErrorTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val uri = URI("https://foo.com")
        val license = URI("https://foo.com/license")
        val scm = URI("https://foo.com/scm")
        val developer = "test developer"
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(developer),
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(developer),
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                modelVersion = "",
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(developer),
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(developer),
                name = "",
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(developer),
                description = "",
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(developer),
                tag = "",
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(),
                scm = scm,
                developers = setOf(developer),
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(),
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            issuer.pom(
                version = "baz",
                packaging = "jar",
                uri = uri,
                licenses = setOf(license),
                scm = scm,
                developers = setOf(""),
            )
        }
    }

    @Test
    fun snapshotMetadataTest() {
        val group = "com.github.foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val actual = Maven.Snapshot.metadata(artifact = issuer)
        val expected = URI("https://central.sonatype.com/repository/maven-snapshots/com/github/foo/$id/maven-metadata.xml")
        assertEquals(expected, actual)
    }

    @Test
    fun assembleTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val version = "baz"
        val projectDir = Files.createTempDirectory(this::class.java.name).toFile().let(FileUtils::canonicalize)
        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        val target = project.layout.projectDirectory.file("foo")
        val expected = """
            repository:
             groupId: '$group'
             artifactId: '$id'
            version: '$version'
        """.trimIndent()
        val actual = issuer.assemble(version = version, target = target).readText()
        assertEquals(expected, actual)
    }

    @Test
    fun assembleErrorTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
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
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val expected = "Artifact($group/$id)"
        val actual = issuer.toString()
        assertEquals(expected, actual)
    }

    @Test
    fun hashCodeTest() {
        val group = "foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val expected = Objects.hash(group, id)
        val actual = issuer.hashCode()
        assertEquals(expected, actual)
    }

    @Test
    fun equalsTest() {
        val group = "foo"
        val id = "bar"
        val i0 = Maven.Artifact(group = group, id = id)
        val i1 = Maven.Artifact(group = group, id = id)
        val i2 = Maven.Artifact(group = id, id = group)
        val i3 = Maven.Artifact(group = group, id = group)
        assertEquals(true, i0 == i1)
        assertEquals(false, i0 == i2)
        assertEquals(false, i0 == i3)
        assertEquals(false, i0.equals(Unit))
    }
}
