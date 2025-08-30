import converter.TokenConverterRegistry
import converter.specific.AssignmentToToken
import converter.specific.ComparisonToToken
import converter.specific.ConditionalToToken
import converter.specific.DataTypeToToken
import converter.specific.DeclarationToToken
import converter.specific.DelimiterToToken
import converter.specific.FunctionToToken
import converter.specific.LogicalOperatorToken
import converter.specific.LoopToToken
import converter.specific.NumberLiteralToToken
import converter.specific.OperatorToToken
import converter.specific.PrintToToken
import converter.specific.ReturnToToken
import converter.specific.StringLiteralToToken
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TokenConverterTest {
    private val position = Position(1, 1)

    @Test
    fun `test DelimiterToToken`() {
        Assertions.assertTrue(DelimiterToToken.canHandle("("))
        Assertions.assertTrue(DelimiterToToken.canHandle(")"))
        Assertions.assertTrue(DelimiterToToken.canHandle("{"))
        Assertions.assertTrue(DelimiterToToken.canHandle("}"))
        Assertions.assertTrue(DelimiterToToken.canHandle(","))
        Assertions.assertTrue(DelimiterToToken.canHandle("."))
        Assertions.assertTrue(DelimiterToToken.canHandle(";"))
        Assertions.assertTrue(DelimiterToToken.canHandle(":"))
        Assertions.assertTrue(DelimiterToToken.canHandle("?"))
        Assertions.assertFalse(DelimiterToToken.canHandle("a"))
        val token = DelimiterToToken.convert("(", position)
        Assertions.assertEquals(TokenType.DELIMITERS, token.getType())
    }

    @Test
    fun `test OperatorToToken`() {
        Assertions.assertTrue(OperatorToToken.canHandle("+"))
        Assertions.assertTrue(OperatorToToken.canHandle("-"))
        Assertions.assertTrue(OperatorToToken.canHandle("*"))
        Assertions.assertTrue(OperatorToToken.canHandle("/"))
        Assertions.assertFalse(OperatorToToken.canHandle("="))
        val token = OperatorToToken.convert("+", position)
        Assertions.assertEquals(TokenType.OPERATORS, token.getType())
    }

    @Test
    fun `test AssignmentToToken`() {
        Assertions.assertTrue(AssignmentToToken.canHandle("="))
        Assertions.assertFalse(AssignmentToToken.canHandle("=="))
        val token = AssignmentToToken.convert("=", position)
        Assertions.assertEquals(TokenType.ASSIGNMENT, token.getType())
    }

    @Test
    fun `test ComparisonToToken`() {
        Assertions.assertTrue(ComparisonToToken.canHandle("=="))
        Assertions.assertTrue(ComparisonToToken.canHandle("!="))
        Assertions.assertTrue(ComparisonToToken.canHandle(">"))
        Assertions.assertTrue(ComparisonToToken.canHandle(">="))
        Assertions.assertTrue(ComparisonToToken.canHandle("<"))
        Assertions.assertTrue(ComparisonToToken.canHandle("<="))
        Assertions.assertFalse(ComparisonToToken.canHandle("="))
        val token = ComparisonToToken.convert("==", position)
        Assertions.assertEquals(TokenType.COMPARISON, token.getType())
    }

    @Test
    fun `test LogicalOperatorToken`() {
        Assertions.assertTrue(LogicalOperatorToken.canHandle("&&"))
        Assertions.assertTrue(LogicalOperatorToken.canHandle("||"))
        Assertions.assertFalse(LogicalOperatorToken.canHandle("&"))
        val token = LogicalOperatorToken.convert("&&", position)
        Assertions.assertEquals(TokenType.LOGICAL_OPERATORS, token.getType())
    }

    @Test
    fun `test ConditionalToToken`() {
        Assertions.assertTrue(ConditionalToToken.canHandle("if"))
        Assertions.assertTrue(ConditionalToToken.canHandle("else"))
        Assertions.assertFalse(ConditionalToToken.canHandle("for"))
        val token = ConditionalToToken.convert("if", position)
        Assertions.assertEquals(TokenType.CONDITIONALS, token.getType())
    }

    @Test
    fun `test LoopToToken`() {
        Assertions.assertTrue(LoopToToken.canHandle("for"))
        Assertions.assertTrue(LoopToToken.canHandle("while"))
        Assertions.assertFalse(LoopToToken.canHandle("if"))
        val token = LoopToToken.convert("for", position)
        Assertions.assertEquals(TokenType.LOOPS, token.getType())
    }

    @Test
    fun `test FunctionToToken`() {
        Assertions.assertTrue(FunctionToToken.canHandle("function"))
        Assertions.assertFalse(FunctionToToken.canHandle("func"))
        val token = FunctionToToken.convert("function", position)
        Assertions.assertEquals(TokenType.FUNCTION, token.getType())
    }

    @Test
    fun `test PrintToToken`() {
        Assertions.assertTrue(PrintToToken.canHandle("println"))
        Assertions.assertFalse(PrintToToken.canHandle("print"))
        val token = PrintToToken.convert("println", position)
        Assertions.assertEquals(TokenType.PRINT, token.getType())
    }

    @Test
    fun `test ReturnToToken`() {
        Assertions.assertTrue(ReturnToToken.canHandle("return"))
        Assertions.assertFalse(ReturnToToken.canHandle("ret"))
        val token = ReturnToToken.convert("return", position)
        Assertions.assertEquals(TokenType.RETURN, token.getType())
    }

    @Test
    fun `test DeclarationToToken`() {
        Assertions.assertTrue(DeclarationToToken.canHandle("let"))
        val token = DeclarationToToken.convert("let", position)
        Assertions.assertEquals(TokenType.DECLARATION, token.getType())
    }

    @Test
    fun `test NumberToToken`() {
        Assertions.assertTrue(DataTypeToToken.canHandle("Number"))
        Assertions.assertTrue(DataTypeToToken.canHandle("String"))
        val token = DataTypeToToken.convert("Number", position)
        Assertions.assertEquals(TokenType.DATA_TYPES, token.getType())
    }

    @Test
    fun `test StringToToken`() {
        Assertions.assertTrue(DataTypeToToken.canHandle("String"))
        Assertions.assertTrue(DataTypeToToken.canHandle("Number"))
        val token = DataTypeToToken.convert("string", position)
        Assertions.assertEquals(TokenType.DATA_TYPES, token.getType())
    }

    @Test
    fun `test NumberLiteralToToken`() {
        Assertions.assertTrue(NumberLiteralToToken.canHandle("123"))
        Assertions.assertTrue(NumberLiteralToToken.canHandle("123.45"))
        Assertions.assertFalse(NumberLiteralToToken.canHandle("abc"))
        val token = NumberLiteralToToken.convert("123", position)
        Assertions.assertEquals(TokenType.NUMBER_LITERAL, token.getType())
    }

    @Test
    fun `test StringLiteralToToken`() {
        Assertions.assertTrue(StringLiteralToToken.canHandle("'hello'"))
        Assertions.assertTrue(StringLiteralToToken.canHandle("\"hello\""))
        Assertions.assertFalse(StringLiteralToToken.canHandle("hello"))
        val token = StringLiteralToToken.convert("'hello'", position)
        Assertions.assertEquals(TokenType.STRING_LITERAL, token.getType())
    }

    @Test
    fun `test TokenConverterRegistry`() {
        val converters =
            listOf(
                NumberLiteralToToken,
                StringLiteralToToken,
                DelimiterToToken,
            )
        val registry = TokenConverterRegistry(converters)

        val openParen = registry.convert("(", position)
        Assertions.assertEquals(TokenType.DELIMITERS, openParen.getType())

        val number = registry.convert("123", position)
        Assertions.assertEquals(TokenType.NUMBER_LITERAL, number.getType())

        val identifier = registry.convert("someVar", position)
        Assertions.assertEquals(TokenType.IDENTIFIER, identifier.getType())
    }
}
