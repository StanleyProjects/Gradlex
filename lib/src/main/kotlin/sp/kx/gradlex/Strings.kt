package sp.kx.gradlex

fun camelCase(n0: String, n1: String, vararg other: String): String {
    require(n0.isNotBlank()) { "The first segment is blank!" }
    val builder = StringBuilder(n0)
    builder.append(n1.replaceFirstChar(Char::titlecase))
    for (it in other) {
        if (it.isNotBlank()) {
            builder.append(it.replaceFirstChar(Char::titlecase))
        }
    }
    return builder.toString()
}
