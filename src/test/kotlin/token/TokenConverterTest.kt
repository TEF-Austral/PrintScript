package token

import lexer.StringToPrintScriptToken
import lexer.TokenConverter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenConverterTest {
    private val tokenConverter: TokenConverter = StringToPrintScriptToken(TokenTypeFactory().createDefaultMap())

    @Test
    fun `test keyword token conversion`() {
        assertEquals(TokenType.LET, tokenConverter.convert("let", Position(1, 1)).getType())
        assertEquals(TokenType.IF, tokenConverter.convert("if", Position(1, 1)).getType())
        assertEquals(TokenType.ELSE, tokenConverter.convert("else", Position(1, 1)).getType())
        assertEquals(TokenType.WHILE, tokenConverter.convert("while", Position(1, 1)).getType())
        assertEquals(TokenType.FOR, tokenConverter.convert("for", Position(1, 1)).getType())
        assertEquals(TokenType.RETURN, tokenConverter.convert("return", Position(1, 1)).getType())
        assertEquals(TokenType.FUNCTION, tokenConverter.convert("function", Position(1, 1)).getType())
        assertEquals(TokenType.BOOLEAN_LITERAL, tokenConverter.convert("true", Position(1, 1)).getType())
        assertEquals(TokenType.BOOLEAN_LITERAL, tokenConverter.convert("false", Position(1, 1)).getType())
        assertEquals(TokenType.CONST, tokenConverter.convert("const", Position(1, 1)).getType())
    }

    @Test
    fun `test operator token conversion`() {
        assertEquals(TokenType.PLUS, tokenConverter.convert("+", Position(1, 1)).getType())
        assertEquals(TokenType.MINUS, tokenConverter.convert("-", Position(1, 1)).getType())
        assertEquals(TokenType.MULTIPLY, tokenConverter.convert("*", Position(1, 1)).getType())
        assertEquals(TokenType.DIVIDE, tokenConverter.convert("/", Position(1, 1)).getType())
        assertEquals(TokenType.ASSIGN, tokenConverter.convert("=", Position(1, 1)).getType())
    }

    @Test
    fun `test punctuation token conversion`() {
        assertEquals(TokenType.SEMICOLON, tokenConverter.convert(";", Position(1, 1)).getType())
        assertEquals(TokenType.COMMA, tokenConverter.convert(",", Position(1, 1)).getType())
        assertEquals(TokenType.COLON, tokenConverter.convert(":", Position(1, 1)).getType())
        assertEquals(TokenType.OPEN_PARENTHESIS, tokenConverter.convert("(", Position(1, 1)).getType())
        assertEquals(TokenType.CLOSE_PARENTHESIS, tokenConverter.convert(")", Position(1, 1)).getType())
        assertEquals(TokenType.OPEN_BRACE, tokenConverter.convert("{", Position(1, 1)).getType())
        assertEquals(TokenType.CLOSE_BRACE, tokenConverter.convert("}", Position(1, 1)).getType())
    }

    @Test
    fun `test literal token conversion`() {
        assertEquals(TokenType.NUMBER_LITERAL, tokenConverter.convert("123", Position(1, 1)).getType())
        assertEquals(TokenType.NUMBER_LITERAL, tokenConverter.convert("123.45", Position(1, 1)).getType())
        assertEquals(TokenType.STRING_LITERAL, tokenConverter.convert("'hello'", Position(1, 1)).getType())
    }

    @Test
    fun `test identifier token conversion`() {
        assertEquals(TokenType.IDENTIFIER, tokenConverter.convert("myVar", Position(1, 1)).getType())
    }
}