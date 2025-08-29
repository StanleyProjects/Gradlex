package sp.kx.gradlex

import java.net.URI

/**
 * A set of functions for working with [Markdown](https://en.wikipedia.org/wiki/Markdown) markup language.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
object Markdown {
    /**
     * @return a URI with a description in [link](https://docs.github.com/en/get-started/writing-on-github/getting-started-with-writing-and-formatting-on-github/basic-writing-and-formatting-syntax#links) format.
     * @param text The description.
     * @param uri The value of the link.
     * @throws IllegalArgumentException if [text] is blank.
     * @author [Stanley Wintergreen](https://github.com/kepocnhh)
     * @since 0.1.0
     */
    fun link(
        text: String,
        uri: URI,
    ): String {
        require(text.isNotBlank()) { "The text is blank!" }
        return "[$text]($uri)"
    }
}
