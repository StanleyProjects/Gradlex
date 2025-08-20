package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringsTest {
    @Test
    fun camelCaseTest() {
        assertEquals("foo", camelCase("foo"))
    }
}
