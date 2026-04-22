package sp.kx.gradlex

/**
 * Usage:
 * ```
 * val actual = camelCase("foo", "bar", "baz")
 * assertEquals("fooBarBaz", actual)
 * ```
 * @return The string from first [n0], not blank [n1] and all the not blank capitalized [other] segments.
 * @throws IllegalArgumentException if [n0] is blank.
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.1.0
 */
fun camelCase(n0: String, n1: String, vararg other: String): String {
    require(n0.isNotBlank()) { "The first segment is blank!" }
    val builder = StringBuilder(n0)
    builder.append(n1.ufc())
    for (it in other) {
        if (it.isNotBlank()) {
            builder.append(it.ufc())
        }
    }
    return builder.toString()
}

/**
 * Usage:
 * ```
 * val actual = "fooBarBaz".ufc()
 * assertEquals("FooBarBaz", actual)
 * ```
 * @return The [String] in which the first character in [this] receiver to the [Character.toTitleCase].
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.2.1
 */
fun String.tfc(): String {
    return replaceFirstChar(Character::toTitleCase)
}

/**
 * Usage:
 * ```
 * val actual = "fooBarBaz".ufc()
 * assertEquals("FooBarBaz", actual)
 * ```
 * @return The [String] in which the first character in [this] receiver to the [Character.toUpperCase].
 * @author [Stanley Wintergreen](https://github.com/kepocnhh)
 * @since 0.2.1
 */
fun String.ufc(): String {
    return replaceFirstChar(Character::toUpperCase)
}
