package sp.kx.gradlex

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class StringsTest {
    @Test
    fun camelCaseTest() {
        val expected = "fooBarBaz"
        val actual = camelCase("foo", "", "bar", " ", "baz")
        assertEquals(expected, actual)
    }

    @Test
    fun camelCaseErrorTest() {
        val expected = "The first segment is blank!"
        val actual = assertThrows(IllegalArgumentException::class.java) {
            camelCase("", "")
        }.message
        assertEquals(expected, actual)
    }

    @Test
    fun tfcTest() {
        val expected = "FooBarBaz"
        val actual = "fooBarBaz".tfc()
        assertEquals(expected, actual)
    }

    @Test
    fun ufcTest() {
        val expected = "FooBarBaz"
        val actual = "fooBarBaz".ufc()
        assertEquals(expected, actual)
    }
}
