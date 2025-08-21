package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MavenTest {
    @Test
    fun nameTest() {
        val group = "foo"
        val id = "bar"
        val version = "baz"
        val issuer = Maven.Artifact(group = group, id = id)
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
        val version = "baz"
        val issuer = Maven.Artifact(group = group, id = id)
        val expected = "$group:$id:$version"
        val actual = issuer.moduleName(version = version)
        assertEquals(expected, actual)
    }
}
