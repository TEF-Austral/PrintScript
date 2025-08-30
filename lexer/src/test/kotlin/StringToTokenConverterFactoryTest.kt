import converter.StringToTokenConverterFactory
import converter.specific.DelimiterToToken
import converter.specific.NumberLiteralToToken
import converter.specific.OperatorToToken
import converter.specific.StringLiteralToToken
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringToTokenConverterFactoryTest {
    private val position = Position(1, 1)

    @Test
    fun `createDefaultsTokenConverter should create converter with all default converters`() {
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()

        val letToken = tokenConverter.convert("let", position)
        assertEquals(TokenType.DECLARATION, letToken.getType())

        val numberToken = tokenConverter.convert("42", position)
        assertEquals(TokenType.NUMBER_LITERAL, numberToken.getType())

        val stringToken = tokenConverter.convert("\"hello\"", position)
        assertEquals(TokenType.STRING_LITERAL, stringToken.getType())

        val operatorToken = tokenConverter.convert("+", position)
        assertEquals(TokenType.OPERATORS, operatorToken.getType())

        val delimiterToken = tokenConverter.convert("(", position)
        assertEquals(TokenType.DELIMITERS, delimiterToken.getType())

        val conditionalToken = tokenConverter.convert("if", position)
        assertEquals(TokenType.CONDITIONALS, conditionalToken.getType())

        val functionToken = tokenConverter.convert("function", position)
        assertEquals(TokenType.FUNCTION, functionToken.getType())
    }

    @Test
    fun `createCustomTokenConverter should only recognize specified converters`() {
        val customConverters =
            listOf(
                NumberLiteralToToken,
                StringLiteralToToken,
                DelimiterToToken,
                OperatorToToken,
            )

        val customTokenConverter = StringToTokenConverterFactory.createCustomTokenConverter(customConverters)

        val numberToken = customTokenConverter.convert("123", position)
        assertEquals(TokenType.NUMBER_LITERAL, numberToken.getType())

        val stringToken = customTokenConverter.convert("'test'", position)
        assertEquals(TokenType.STRING_LITERAL, stringToken.getType())

        val delimiterToken = customTokenConverter.convert(")", position)
        assertEquals(TokenType.DELIMITERS, delimiterToken.getType())

        val operatorToken = customTokenConverter.convert("*", position)
        assertEquals(TokenType.OPERATORS, operatorToken.getType())

        val declarationToken = customTokenConverter.convert("let", position)
        assertEquals(TokenType.IDENTIFIER, declarationToken.getType())

        val conditionalToken = customTokenConverter.convert("if", position)
        assertEquals(TokenType.IDENTIFIER, conditionalToken.getType())

        val functionToken = customTokenConverter.convert("function", position)
        assertEquals(TokenType.IDENTIFIER, functionToken.getType())
    }

    @Test
    fun `createCustomTokenConverter with empty list should only recognize identifiers`() {
        val customTokenConverter = StringToTokenConverterFactory.createCustomTokenConverter(emptyList())

        val token1 = customTokenConverter.convert("let", position)
        assertEquals(TokenType.IDENTIFIER, token1.getType())

        val token2 = customTokenConverter.convert("123", position)
        assertEquals(TokenType.IDENTIFIER, token2.getType())

        val token3 = customTokenConverter.convert("if", position)
        assertEquals(TokenType.IDENTIFIER, token3.getType())

        val token4 = customTokenConverter.convert("+", position)
        assertEquals(TokenType.IDENTIFIER, token4.getType())
    }

    @Test
    fun `createCustomTokenConverter with single converter should only recognize that type`() {
        val customConverters = listOf(NumberLiteralToToken)
        val customTokenConverter = StringToTokenConverterFactory.createCustomTokenConverter(customConverters)

        val numberToken = customTokenConverter.convert("456", position)
        assertEquals(TokenType.NUMBER_LITERAL, numberToken.getType())

        val floatToken = customTokenConverter.convert("3.14", position)
        assertEquals(TokenType.NUMBER_LITERAL, floatToken.getType())

        val stringToken = customTokenConverter.convert("\"hello\"", position)
        assertEquals(TokenType.IDENTIFIER, stringToken.getType())

        val operatorToken = customTokenConverter.convert("+", position)
        assertEquals(TokenType.IDENTIFIER, operatorToken.getType())

        val delimiterToken = customTokenConverter.convert("(", position)
        assertEquals(TokenType.IDENTIFIER, delimiterToken.getType())
    }
}
