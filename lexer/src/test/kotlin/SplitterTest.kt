import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SplitterTest {

    @Test
    fun `splits input into tokens with correct coordinates`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';'))
        val input = "let x = 10;\nlet y = x + 5;"
        val tokens = splitter.split(input)

        assertEquals(12, tokens.size)
        assertEquals("let", tokens[0].first)
        assertEquals(1, tokens[0].second.getRow())
        assertEquals(1, tokens[0].second.getColumn())

        assertEquals("x", tokens[1].first)
        assertEquals(1, tokens[1].second.getRow())
        assertEquals(5, tokens[1].second.getColumn())

        assertEquals("=", tokens[2].first)
        assertEquals(1, tokens[2].second.getRow())
        assertEquals(7, tokens[2].second.getColumn())

        assertEquals("10", tokens[3].first)
        assertEquals(1, tokens[3].second.getRow())
        assertEquals(9, tokens[3].second.getColumn())

        assertEquals(";", tokens[4].first)
        assertEquals(1, tokens[4].second.getRow())
        assertEquals(11, tokens[4].second.getColumn())

        assertEquals("let", tokens[5].first)
        assertEquals(2, tokens[5].second.getRow())
        assertEquals(1, tokens[5].second.getColumn())

        assertEquals("y", tokens[6].first)
        assertEquals(2, tokens[6].second.getRow())
        assertEquals(5, tokens[6].second.getColumn())

        assertEquals("=", tokens[7].first)
        assertEquals(2, tokens[7].second.getRow())
        assertEquals(7, tokens[7].second.getColumn())

        assertEquals("x", tokens[8].first)
        assertEquals(2, tokens[8].second.getRow())
        assertEquals(9, tokens[8].second.getColumn())

        assertEquals("+", tokens[9].first)
        assertEquals(2, tokens[9].second.getRow())
        assertEquals(11, tokens[9].second.getColumn())

        assertEquals("5", tokens[10].first)
        assertEquals(2, tokens[10].second.getRow())
        assertEquals(13, tokens[10].second.getColumn())

        assertEquals(";", tokens[11].first)
        assertEquals(2, tokens[11].second.getRow())
        assertEquals(14, tokens[11].second.getColumn())
    }

    @Test
    fun `handles input with only whitespace`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';'))
        val input = " \n\t "
        val tokens = splitter.split(input)
        assertEquals(0, tokens.size)
    }

    @Test
    fun `handles input with special characters only`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';'))
        val input = "=+*/;"
        val tokens = splitter.split(input)

        assertEquals(5, tokens.size)
        assertEquals("=", tokens[0].first)
        assertEquals("+", tokens[1].first)
        assertEquals("*", tokens[2].first)
        assertEquals("/", tokens[3].first)
        assertEquals(";", tokens[4].first)
    }

    @Test
    fun `handles input with mixed words and special characters`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';'))
        val input = "var1=var2+3;"
        val tokens = splitter.split(input)

        assertEquals(6, tokens.size)
        assertEquals("var1", tokens[0].first)
        assertEquals("=", tokens[1].first)
        assertEquals("var2", tokens[2].first)
        assertEquals("+", tokens[3].first)
        assertEquals("3", tokens[4].first)
        assertEquals(";", tokens[5].first)
    }

    @Test
    fun `handles input with unsupported special characters`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';'))
        val input = "a@b#c"
        val tokens = splitter.split(input)

        assertEquals(1, tokens.size)
        assertEquals("a@b#c", tokens[0].first)
    }

    @Test
    fun `handles empty input`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';'))
        val input = ""
        val tokens = splitter.split(input)
        assertEquals(0, tokens.size)
    }

    @Test
    fun `handles !=`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';', '!', '<', '>'))
        val input = "a != b;"
        val tokens = splitter.split(input)
        print(tokens)

        assertEquals(4, tokens.size)
        assertEquals("a", tokens[0].first)
        assertEquals("!=", tokens[1].first)
        assertEquals("b", tokens[2].first)
        assertEquals(";", tokens[3].first)
    }

    @Test
    fun `handles complex expressions with multiple operators`() {
        val splitter = StringSplitter(listOf('=', '+', '-', '*', '/', ';', '!', '<', '>', '(', ')'))
        val input = "x = 5 + 3 * (2 - 1);"
        val tokens = splitter.split(input)

        assertEquals(12, tokens.size)
        assertEquals("x", tokens[0].first)
        assertEquals("=", tokens[1].first)
        assertEquals("5", tokens[2].first)
        assertEquals("+", tokens[3].first)
        assertEquals("3", tokens[4].first)
        assertEquals("*", tokens[5].first)
        assertEquals("(", tokens[6].first)
        assertEquals("2", tokens[7].first)
        assertEquals("-", tokens[8].first)
        assertEquals("1", tokens[9].first)
        assertEquals(")", tokens[10].first)
        assertEquals(";", tokens[11].first)
    }


}