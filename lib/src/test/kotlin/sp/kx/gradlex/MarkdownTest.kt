package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

internal class MarkdownTest {
    @Test
    fun linkTest() {
        val text = "foo"
        val spec = "bar"
        val expected = "[$text]($spec)"
        val actual = Markdown.link(text = text, uri = URI(spec))
        assertEquals(expected, actual)
    }
}
