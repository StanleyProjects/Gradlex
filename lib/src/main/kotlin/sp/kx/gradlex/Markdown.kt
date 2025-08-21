package sp.kx.gradlex

import java.net.URI

object Markdown {
    fun link(
        text: String,
        uri: URI,
    ): String {
        require(text.isNotBlank()) { "The text is blank!" }
        return "[$text]($uri)"
    }
}
