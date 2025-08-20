package sp.kx.gradlex

fun camelCase(segment: String, vararg other: String): String {
    require(segment.isNotBlank()) { "The first segment is blank!" }
    val builder = StringBuilder(segment)
    for (it in other) {
        if (it.isNotBlank()) {
            builder.append(it.replaceFirstChar(Char::titlecase))
        }
    }
    return builder.toString()
}
