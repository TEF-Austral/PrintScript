package token

import lexer.StringToPrintScriptToken
import lexer.TokenConverter
import org.junit.jupiter.api.Test



class TokenConverterTest {
    val tokenConverter: TokenConverter = StringToPrintScriptToken(TokenTypeFactory().createDefaultMap())
    assertEquals(TokenType.LET, tokenConverter.convert("let", Position(1,1)))
    assertEquals(TokenType.IF, tokenConverter.convert("if", Position(1,1)))
    assertEquals(TokenType.ELSE, tokenConverter.convert("else", Position(1,1)))
    assertEquals(TokenType.WHILE, tokenConverter.convert("while", Position(1,1)))
    assertEquals(TokenType.FOR, tokenConverter.convert("for", Position(1,1)))
    assertEquals(TokenType.RETURN, tokenConverter.convert("return", Position(1,1)))
    assertEquals(TokenType.FUNCTION, tokenConverter.convert("function", Position(1,1)))
    assertEquals(TokenType.TRUE, tokenConverter.convert("true", Position(1,1)))
    assertEquals(TokenType.FALSE, tokenConverter.convert("false", Position(1,1)))
    assertEquals(TokenType.CONST, tokenConverter.convert("const", Position(1,1)))
    assertEquals(TokenType.PLUS, tokenConverter.convert("+", Position(1,1)))
    assertEquals(TokenType.MINUS, tokenConverter.convert("-", Position(1,1)))
    assertEquals(TokenType.MULTIPLY, tokenConverter.convert("*", Position(1,1)))
    assertEquals(TokenType.DIVIDE, tokenConverter.convert("/", Position(1,1)))
    assertEquals(TokenType.ASSIGN, tokenConverter.convert("=", Position(1,1)))
    assertEquals(TokenType.SEMICOLON, tokenConverter.convert(";", Position(1,1)))
    assertEquals(TokenType.COMMA, tokenConverter.convert(",", Position(1,1)))
    assertEquals(TokenType.COLON, tokenConverter.convert(":", Position(1,1)))
    assertEquals(TokenType.OPEN_PARENTHESIS, tokenConverter.convert("(", Position(1,1)))
    assertEquals(TokenType.CLOSE_PARENTHESIS, tokenConverter.convert(")", Position(1,1)))
    assertEquals(TokenType.OPEN_BRACE, tokenConverter.convert("{", Position(1,1)))
    assertEquals(TokenType.CLOSE_BRACE, tokenConverter.convert("}", Position(1,1)))
    assertEquals(TokenType.INTEGER_LITERAL, tokenConverter.convert("123", Position(1,1)))
    assertEquals(TokenType.FLOAT_LITERAL, tokenConverter.convert("123.45", Position(1,1)))
    assertEquals(TokenType.STRING_LITERAL, tokenConverter.convert("'hello", Position(1,1)))
    assertEquals(TokenType.IDENTIFIER, tokenConverter.convert("myVar", Position(1,1)))
}