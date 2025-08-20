package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

internal class GitHubTest {
    @Test
    fun releaseTest() {
        val owner = "foo"
        val name = "foo"
        val version = "baz"
        val gh = GitHub.Repository(owner = owner, name = name)
        val expected = URI("https://github.com/$owner/$name/releases/tag/$version")
        val actual = gh.release(version = version)
        assertEquals(expected, actual)
    }
}
