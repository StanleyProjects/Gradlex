package sp.kx.gradlex

import java.net.URI

/**
 * A set of functions for working with [Markdown](https://en.wikipedia.org/wiki/Markdown) markup language.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
object Markdown {
    fun link(
        text: String,
        uri: URI,
    ): String {
        require(text.isNotBlank()) { "The text is blank!" }
        return "[$text]($uri)"
    }
}
