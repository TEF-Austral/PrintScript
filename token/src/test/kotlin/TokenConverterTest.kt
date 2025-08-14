import Coordinates
import converter.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TokenConverterTest {

    private val position = Position(1, 1)

    @Test
    fun `test DelimiterToToken`() {
        assertTrue(DelimiterToToken.canHandle("("))
        assertTrue(DelimiterToToken.canHandle(")"))
        assertTrue(DelimiterToToken.canHandle("{"))
        assertTrue(DelimiterToToken.canHandle("}"))
        assertTrue(DelimiterToToken.canHandle(","))
        assertTrue(DelimiterToToken.canHandle("."))
        assertTrue(DelimiterToToken.canHandle(";"))
        assertTrue(DelimiterToToken.canHandle(":"))
        assertTrue(DelimiterToToken.canHandle("?"))
        assertFalse(DelimiterToToken.canHandle("a"))
        val token = DelimiterToToken.convert("(", position)
        assertEquals(TokenType.DELIMITERS, token.getType())
    }

    @Test
    fun `test OperatorToToken`() {
        assertTrue(OperatorToToken.canHandle("+"))
        assertTrue(OperatorToToken.canHandle("-"))
        assertTrue(OperatorToToken.canHandle("*"))
        assertTrue(OperatorToToken.canHandle("/"))
        assertFalse(OperatorToToken.canHandle("="))
        val token = OperatorToToken.convert("+", position)
        assertEquals(TokenType.OPERATORS, token.getType())
    }

    @Test
    fun `test AssignmentToToken`() {
        assertTrue(AssignmentToToken.canHandle("="))
        assertFalse(AssignmentToToken.canHandle("=="))
        val token = AssignmentToToken.convert("=", position)
        assertEquals(TokenType.ASSIGNMENT, token.getType())
    }

    @Test
    fun `test ComparisonToToken`() {
        assertTrue(ComparisonToToken.canHandle("=="))
        assertTrue(ComparisonToToken.canHandle("!="))
        assertTrue(ComparisonToToken.canHandle(">"))
        assertTrue(ComparisonToToken.canHandle(">="))
        assertTrue(ComparisonToToken.canHandle("<"))
        assertTrue(ComparisonToToken.canHandle("<="))
        assertFalse(ComparisonToToken.canHandle("="))
        val token = ComparisonToToken.convert("==", position)
        assertEquals(TokenType.COMPARISON, token.getType())
    }

    @Test
    fun `test LogicalOperatorToken`() {
        assertTrue(LogicalOperatorToken.canHandle("&&"))
        assertTrue(LogicalOperatorToken.canHandle("||"))
        assertFalse(LogicalOperatorToken.canHandle("&"))
        val token = LogicalOperatorToken.convert("&&", position)
        assertEquals(TokenType.LOGICAL_OPERATORS, token.getType())
    }

    @Test
    fun `test ConditionalToToken`() {
        assertTrue(ConditionalToToken.canHandle("if"))
        assertTrue(ConditionalToToken.canHandle("else"))
        assertFalse(ConditionalToToken.canHandle("for"))
        val token = ConditionalToToken.convert("if", position)
        assertEquals(TokenType.CONDITIONALS, token.getType())
    }

    @Test
    fun `test LoopToToken`() {
        assertTrue(LoopToToken.canHandle("for"))
        assertTrue(LoopToToken.canHandle("while"))
        assertFalse(LoopToToken.canHandle("if"))
        val token = LoopToToken.convert("for", position)
        assertEquals(TokenType.LOOPS, token.getType())
    }

    @Test
    fun `test FunctionToToken`() {
        assertTrue(FunctionToToken.canHandle("function"))
        assertFalse(FunctionToToken.canHandle("func"))
        val token = FunctionToToken.convert("function", position)
        assertEquals(TokenType.FUNCTION, token.getType())
    }

    @Test
    fun `test PrintToToken`() {
        assertTrue(PrintToToken.canHandle("println"))
        assertFalse(PrintToToken.canHandle("print"))
        val token = PrintToToken.convert("println", position)
        assertEquals(TokenType.PRINT, token.getType())
    }

    @Test
    fun `test ReturnToToken`() {
        assertTrue(ReturnToToken.canHandle("return"))
        assertFalse(ReturnToToken.canHandle("ret"))
        val token = ReturnToToken.convert("return", position)
        assertEquals(TokenType.RETURN, token.getType())
    }

    @Test
    fun `test DeclarationToToken`() {
        assertTrue(DeclarationToToken.canHandle("let"))
        val token = DeclarationToToken.convert("let", position)
        assertEquals(TokenType.DECLARATION, token.getType())
    }

    @Test
    fun `test NumberToToken`() {
        assertTrue(NumberToToken.canHandle("number"))
        assertFalse(NumberToToken.canHandle("string"))
        val token = NumberToToken.convert("number", position)
        assertEquals(TokenType.NUMBER, token.getType())
    }

    @Test
    fun `test StringToToken`() {
        assertTrue(StringToToken.canHandle("string"))
        assertFalse(StringToToken.canHandle("number"))
        val token = StringToToken.convert("string", position)
        assertEquals(TokenType.STRING, token.getType())
    }

    @Test
    fun `test NumberLiteralToToken`() {
        assertTrue(NumberLiteralToToken.canHandle("123"))
        assertTrue(NumberLiteralToToken.canHandle("123.45"))
        assertFalse(NumberLiteralToToken.canHandle("abc"))
        val token = NumberLiteralToToken.convert("123", position)
        assertEquals(TokenType.NUMBER_LITERAL, token.getType())
    }

    @Test
    fun `test StringLiteralToToken`() {
        assertTrue(StringLiteralToToken.canHandle("'hello'"))
        assertTrue(StringLiteralToToken.canHandle("\"hello\""))
        assertFalse(StringLiteralToToken.canHandle("hello"))
        val token = StringLiteralToToken.convert("'hello'", position)
        assertEquals(TokenType.STRING_LITERAL, token.getType())
    }

    @Test
    fun `test TokenConverterRegistry`() {
        val converters = listOf(
            NumberLiteralToToken,
            StringLiteralToToken,
            DelimiterToToken
        )
        val registry = TokenConverterRegistry(converters)

        val openParen = registry.convert("(", position)
        assertEquals(TokenType.DELIMITERS, openParen.getType())

        val number = registry.convert("123", position)
        assertEquals(TokenType.NUMBER_LITERAL, number.getType())

        val identifier = registry.convert("someVar", position)
        assertEquals(TokenType.IDENTIFIER, identifier.getType())
    }
}