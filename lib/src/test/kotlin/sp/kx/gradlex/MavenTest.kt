package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

internal class MavenTest {
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
    fun snapshotMetadataTest() {
        val group = "com.github.foo"
        val id = "bar"
        val issuer = Maven.Artifact(group = group, id = id)
        val actual = Maven.Snapshot.metadata(artifact = issuer)
        val expected = URI("https://central.sonatype.com/repository/maven-snapshots/com/github/foo/$id/maven-metadata.xml")
        assertEquals(expected, actual)
    }
}
