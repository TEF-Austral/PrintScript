package lexer

import lexer.reader.MockReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import token.TokenType
import token.TokenTypeFactory

class LexerTest {
    //ANDAN COMO EL CULO QUEDA PARA TODO
    private val splitter: Splitter = SplitterFactory().createSplitter()
    private val typeMap = TokenTypeFactory().createDefaultMap()

    @Test
    fun `returns empty list for empty input`() {
        val lexer = PrintScriptLexer(MockReader(""), splitter)
        val tokens = lexer.tokenize(typeMap)
        assertEquals(0, tokens.size)
    }

    @Test
    fun `returns empty list for input with only whitespace`() {
        val lexer = PrintScriptLexer(MockReader(" \n\t "), splitter)
        val tokens = lexer.tokenize(typeMap)
        assertEquals(0, tokens.size)
    }

    @Test
    fun `tokenizes a simple variable declaration correctly`() {
        val lexer = PrintScriptLexer(MockReader("let name = \"John\";"), splitter)
        val tokens = lexer.tokenize(typeMap)

        assertEquals(5, tokens.size)
        assertEquals(TokenType.LET, tokens[0].getType())
        assertEquals(TokenType.IDENTIFIER, tokens[1].getType())
        assertEquals(TokenType.ASSIGN, tokens[2].getType())
        assertEquals(TokenType.STRING_LITERAL, tokens[3].getType())
        assertEquals(TokenType.SEMICOLON, tokens[4].getType())
    }

    @Test
    fun `tokenizes different token types correctly`() {
        val lexer = PrintScriptLexer(MockReader("let x: number = 5.5;"), splitter)
        val tokens = lexer.tokenize(typeMap)

        val expectedTypes = listOf(
            TokenType.LET,
            TokenType.IDENTIFIER,
            TokenType.COLON,
            TokenType.NUMBER,
            TokenType.ASSIGN,
            TokenType.NUMBER_LITERAL,
            TokenType.SEMICOLON
        )
        assertEquals(expectedTypes.size, tokens.size)
        tokens.zip(expectedTypes).forEach { (token, expectedType) ->
            assertEquals(expectedType, token.getType())
        }
    }

    @Test
    fun `handles multi-line input and tracks coordinates correctly`() {
        val lexer = PrintScriptLexer(MockReader("let a = 1;\nlet b = 2;"), splitter)
        val tokens = lexer.tokenize(typeMap)
        print(tokens)

        assertEquals(10, tokens.size)

        // Test first token on line 1
        assertEquals(TokenType.LET, tokens[0].getType())
        assertEquals(1, tokens[0].getCoordinates().getRow())
        assertEquals(1, tokens[0].getCoordinates().getColumn())

        // Test first token on line 2
        assertEquals(TokenType.SEMICOLON, tokens[4].getType())
        assertEquals(1, tokens[4].getCoordinates().getRow())
        assertEquals(10, tokens[4].getCoordinates().getColumn())

        // Test last token on line 2
        assertEquals(TokenType.SEMICOLON, tokens[9].getType())
        assertEquals(2, tokens[7].getCoordinates().getRow())
        assertEquals(11, tokens[7].getCoordinates().getColumn())
    }

    @Test
    fun `tokenizes a complex statement with various symbols`() {
        val lexer = PrintScriptLexer(MockReader("if (x > 5) { print(\"Hello\"); }"), splitter)
        val tokens = lexer.tokenize(typeMap)
        for (token in tokens) {
            println(token.toString())
        }

        val expectedTypes = listOf(
            TokenType.IF,
            TokenType.OPEN_PARENTHESIS,
            TokenType.IDENTIFIER,
            TokenType.GREATER_THAN, // '>' is an identifier
            TokenType.NUMBER_LITERAL,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.OPEN_BRACE,
            TokenType.PRINT, // 'print'
            TokenType.OPEN_PARENTHESIS,
            TokenType.STRING_LITERAL,
            TokenType.CLOSE_PARENTHESIS,
            TokenType.SEMICOLON,
            TokenType.CLOSE_BRACE
        )

        assertEquals(expectedTypes.size, tokens.size)
        tokens.zip(expectedTypes).forEach { (token, expectedType) ->
            assertEquals(expectedType, token.getType())
        }
    }

    @Test
    fun `tokenizes a complex expression with multiple operators`() {
        val lexer = PrintScriptLexer(MockReader("let result = (a + b) * (c - d) / e;"), splitter)
        val tokens = lexer.tokenize(typeMap)

        val expectedTypes = listOf(
            TokenType.LET,
            TokenType.IDENTIFIER,
            TokenType.ASSIGN,
            TokenType.OPEN_PARENTHESIS,
            TokenType.IDENTIFIER, // 'a'
            TokenType.PLUS,
            TokenType.IDENTIFIER, // 'b'
            TokenType.CLOSE_PARENTHESIS,
            TokenType.MULTIPLY,
            TokenType.OPEN_PARENTHESIS,
            TokenType.IDENTIFIER, // 'c'
            TokenType.MINUS,
            TokenType.IDENTIFIER, // 'd'
            TokenType.CLOSE_PARENTHESIS,
            TokenType.DIVIDE,
            TokenType.IDENTIFIER, // 'e'
            TokenType.SEMICOLON
        )

        assertEquals(expectedTypes.size, tokens.size)
        tokens.zip(expectedTypes).forEach { (token, expectedType) ->
            assertEquals(expectedType, token.getType())
        }
    }
}