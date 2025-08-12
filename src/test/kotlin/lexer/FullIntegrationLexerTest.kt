package lexer

import lexer.reader.Reader
import lexer.reader.TxtReader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import token.StringToTokenConverterFactory
import token.TokenType

class FullIntegrationLexerTest {

    private lateinit var reader: Reader

    @BeforeEach
    fun setup() {
        val filePath = "src/test/resources/largerScript.txt"
        reader = TxtReader(filePath)
    }

    data class Expected(
        val type: TokenType,
        val value: String,
        val row: Int,
        val col: Int
    )

    @Test
    fun fullIntegrationLexer_LargeScript() {
        val splitter = SplitterFactory.createSplitter()
        val tokenConverter = StringToTokenConverterFactory.createDefaultsTokenConverter()
        val lexer = PrintScriptLexer(reader, splitter, tokenConverter)
        val tokens = lexer.tokenize()
        
        assertEquals(70, tokens.size)

        val exp = listOf(
            Expected(TokenType.LET, "let", 1, 1),
            Expected(TokenType.IDENTIFIER, "a", 1, 5),
            Expected(TokenType.ASSIGN, "=", 1, 7),
            Expected(TokenType.NUMBER_LITERAL, "20", 1, 9),
            Expected(TokenType.SEMICOLON, ";", 1, 11),

            Expected(TokenType.LET, "let", 2, 1),
            Expected(TokenType.IDENTIFIER, "b", 2, 5),
            Expected(TokenType.ASSIGN, "=", 2, 7),
            Expected(TokenType.NUMBER_LITERAL, "30", 2, 9),
            Expected(TokenType.SEMICOLON, ";", 2, 11),
            
            Expected(TokenType.LET, "let", 3, 1),
            Expected(TokenType.IDENTIFIER, "sum", 3, 5),
            Expected(TokenType.ASSIGN, "=", 3, 9),
            Expected(TokenType.IDENTIFIER, "a", 3, 11),
            Expected(TokenType.PLUS, "+", 3, 13),
            Expected(TokenType.IDENTIFIER, "b", 3, 15),
            Expected(TokenType.SEMICOLON, ";", 3, 16),
            
            Expected(TokenType.LET, "let", 4, 1),
            Expected(TokenType.IDENTIFIER, "subtract", 4, 5),
            Expected(TokenType.ASSIGN, "=", 4, 14),
            Expected(TokenType.IDENTIFIER, "b", 4, 16),
            Expected(TokenType.MINUS, "-", 4, 18),
            Expected(TokenType.IDENTIFIER, "a", 4, 20),
            Expected(TokenType.SEMICOLON, ";", 4, 21),
            
            Expected(TokenType.LET, "let", 5, 1),
            Expected(TokenType.IDENTIFIER, "multiply", 5, 5),
            Expected(TokenType.ASSIGN, "=", 5, 14),
            Expected(TokenType.IDENTIFIER, "a", 5, 16),
            Expected(TokenType.MULTIPLY, "*", 5, 18),
            Expected(TokenType.IDENTIFIER, "b", 5, 20),
            Expected(TokenType.SEMICOLON, ";", 5, 21),

            Expected(TokenType.LET, "let", 6, 1),
            Expected(TokenType.IDENTIFIER, "divide", 6, 5),
            Expected(TokenType.ASSIGN, "=", 6, 12),
            Expected(TokenType.IDENTIFIER, "b", 6, 14),
            Expected(TokenType.DIVIDE, "/", 6, 16),
            Expected(TokenType.IDENTIFIER, "a", 6, 18),
            Expected(TokenType.SEMICOLON, ";", 6, 19),
            
            Expected(TokenType.PRINT, "printLn", 7, 1),
            Expected(TokenType.CLOSE_PARENTHESIS, "(", 7, 8),
            Expected(TokenType.IDENTIFIER, "sum", 7, 9),
            Expected(TokenType.OPEN_PARENTHESIS, ")", 7, 12),
            Expected(TokenType.SEMICOLON, ";", 7, 13),

            Expected(TokenType.PRINT, "printLn", 8, 1),
            Expected(TokenType.CLOSE_PARENTHESIS, "(", 8, 8),
            Expected(TokenType.IDENTIFIER, "subtract", 8, 9),
            Expected(TokenType.OPEN_PARENTHESIS, ")", 8, 17),
            Expected(TokenType.SEMICOLON, ";", 8, 18),

            Expected(TokenType.PRINT, "printLn", 9, 1),
            Expected(TokenType.CLOSE_PARENTHESIS, "(", 9, 8),
            Expected(TokenType.IDENTIFIER, "multiply", 9, 9),
            Expected(TokenType.OPEN_PARENTHESIS, ")", 9, 17),
            Expected(TokenType.SEMICOLON, ";", 9, 18),

            Expected(TokenType.PRINT, "printLn", 10, 1),
            Expected(TokenType.CLOSE_PARENTHESIS, "(", 10, 8),
            Expected(TokenType.IDENTIFIER, "divide", 10, 9),
            Expected(TokenType.OPEN_PARENTHESIS, ")", 10, 15),
            Expected(TokenType.SEMICOLON, ";", 10, 16),

            Expected(TokenType.RETURN, "return", 11, 1),
            Expected(TokenType.IDENTIFIER, "sum", 11, 8),
            Expected(TokenType.SEMICOLON, ";", 11, 11),
            
            Expected(TokenType.RETURN, "return", 12, 1),
            Expected(TokenType.IDENTIFIER, "subtract", 12, 8),
            Expected(TokenType.SEMICOLON, ";", 12, 16),

            Expected(TokenType.RETURN, "return", 13, 1),
            Expected(TokenType.IDENTIFIER, "multiply", 13, 8),
            Expected(TokenType.SEMICOLON, ";", 13, 16),
            
            Expected(TokenType.RETURN, "return", 14, 1),
            Expected(TokenType.IDENTIFIER, "divide", 14, 8),
            Expected(TokenType.SEMICOLON, ";", 14, 14),
        )

        exp.forEachIndexed { i, e ->
            val t = tokens[i]
            assertEquals(e.type, t.getType(), "Type mismatch at index $i")
            assertEquals(e.value, t.getValue(), "Value mismatch at index $i")
            assertEquals(e.row, t.getCoordinates().getRow(), "Row mismatch at index $i")
            assertEquals(e.col, t.getCoordinates().getColumn(), "Column mismatch at index $i")
        }
    }
}
