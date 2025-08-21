package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
}
