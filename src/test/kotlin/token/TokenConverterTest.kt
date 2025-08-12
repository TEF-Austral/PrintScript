package token

import lexer.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TokenConverterTest {

    private val position = Position(1, 1)

    @Test
    fun `test OpenParenthesisToToken`() {
        assertTrue(OpenParenthesisToToken.canHandle("("))
        assertFalse(OpenParenthesisToToken.canHandle(")"))

        val token = OpenParenthesisToToken.convert("(", position)
        assertEquals(TokenType.OPEN_PARENTHESIS, token.getType())
        assertEquals("(", token.getValue())
    }

    @Test
    fun `test CloseParenthesisToToken`() {
        assertTrue(CloseParenthesisToToken.canHandle(")"))
        assertFalse(CloseParenthesisToToken.canHandle("("))

        val token = CloseParenthesisToToken.convert(")", position)
        assertEquals(TokenType.CLOSE_PARENTHESIS, token.getType())
        assertEquals(")", token.getValue())
    }

    @Test
    fun `test OpenBraceToToken`() {
        assertTrue(OpenBraceToToken.canHandle("{"))
        assertFalse(OpenBraceToToken.canHandle("}"))

        val token = OpenBraceToToken.convert("{", position)
        assertEquals(TokenType.OPEN_BRACE, token.getType())
        assertEquals("{", token.getValue())
    }

    @Test
    fun `test CloseBraceToToken`() {
        assertTrue(CloseBraceToToken.canHandle("}"))
        assertFalse(CloseBraceToToken.canHandle("{"))

        val token = CloseBraceToToken.convert("}", position)
        assertEquals(TokenType.CLOSE_BRACE, token.getType())
        assertEquals("}", token.getValue())
    }

    @Test
    fun `test CommaToToken`() {
        assertTrue(CommaToToken.canHandle(","))
        assertFalse(CommaToToken.canHandle("."))

        val token = CommaToToken.convert(",", position)
        assertEquals(TokenType.COMMA, token.getType())
        assertEquals(",", token.getValue())
    }

    @Test
    fun `test DotToToken`() {
        assertTrue(DotToToken.canHandle("."))
        assertFalse(DotToToken.canHandle(","))

        val token = DotToToken.convert(".", position)
        assertEquals(TokenType.DOT, token.getType())
        assertEquals(".", token.getValue())
    }

    @Test
    fun `test SemicolonToToken`() {
        assertTrue(SemicolonToToken.canHandle(";"))
        assertFalse(SemicolonToToken.canHandle(":"))

        val token = SemicolonToToken.convert(";", position)
        assertEquals(TokenType.SEMICOLON, token.getType())
        assertEquals(";", token.getValue())
    }

    @Test
    fun `test ColonToToken`() {
        assertTrue(ColonToToken.canHandle(":"))
        assertFalse(ColonToToken.canHandle(";"))

        val token = ColonToToken.convert(":", position)
        assertEquals(TokenType.COLON, token.getType())
        assertEquals(":", token.getValue())
    }

    @Test
    fun `test QuestionMarkToToken`() {
        assertTrue(QuestionMarkToToken.canHandle("?"))
        assertFalse(QuestionMarkToToken.canHandle("!"))

        val token = QuestionMarkToToken.convert("?", position)
        assertEquals(TokenType.QUESTION_MARK, token.getType())
        assertEquals("?", token.getValue())
    }

    @Test
    fun `test PlusToToken`() {
        assertTrue(PlusToToken.canHandle("+"))
        assertFalse(PlusToToken.canHandle("-"))

        val token = PlusToToken.convert("+", position)
        assertEquals(TokenType.PLUS, token.getType())
        assertEquals("+", token.getValue())
    }

    @Test
    fun `test MinusToToken`() {
        assertTrue(MinusToToken.canHandle("-"))
        assertFalse(MinusToToken.canHandle("+"))

        val token = MinusToToken.convert("-", position)
        assertEquals(TokenType.MINUS, token.getType())
        assertEquals("-", token.getValue())
    }

    @Test
    fun `test MultiplyToToken`() {
        assertTrue(MultiplyToToken.canHandle("*"))
        assertFalse(MultiplyToToken.canHandle("/"))

        val token = MultiplyToToken.convert("*", position)
        assertEquals(TokenType.MULTIPLY, token.getType())
        assertEquals("*", token.getValue())
    }

    @Test
    fun `test DivideToToken`() {
        assertTrue(DivideToToken.canHandle("/"))
        assertFalse(DivideToToken.canHandle("*"))

        val token = DivideToToken.convert("/", position)
        assertEquals(TokenType.DIVIDE, token.getType())
        assertEquals("/", token.getValue())
    }

    @Test
    fun `test AssignToToken`() {
        assertTrue(AssignToToken.canHandle("="))
        assertFalse(AssignToToken.canHandle("=="))

        val token = AssignToToken.convert("=", position)
        assertEquals(TokenType.ASSIGN, token.getType())
        assertEquals("=", token.getValue())
    }

    @Test
    fun `test EqualsToToken`() {
        assertTrue(EqualsToToken.canHandle("=="))
        assertFalse(EqualsToToken.canHandle("="))

        val token = EqualsToToken.convert("==", position)
        assertEquals(TokenType.EQUALS, token.getType())
        assertEquals("==", token.getValue())
    }

    @Test
    fun `test NotEqualsToToken`() {
        assertTrue(NotEqualsToToken.canHandle("!="))
        assertFalse(NotEqualsToToken.canHandle("!"))

        val token = NotEqualsToToken.convert("!=", position)
        assertEquals(TokenType.NOT_EQUALS, token.getType())
        assertEquals("!=", token.getValue())
    }

    @Test
    fun `test GreaterThanToToken`() {
        assertTrue(GreaterThanToToken.canHandle(">"))
        assertFalse(GreaterThanToToken.canHandle(">="))

        val token = GreaterThanToToken.convert(">", position)
        assertEquals(TokenType.GREATER_THAN, token.getType())
        assertEquals(">", token.getValue())
    }

    @Test
    fun `test GreaterThanOrEqualToToken`() {
        assertTrue(GreaterThanOrEqualToToken.canHandle(">="))
        assertFalse(GreaterThanOrEqualToToken.canHandle(">"))

        val token = GreaterThanOrEqualToToken.convert(">=", position)
        assertEquals(TokenType.GREATER_THAN_OR_EQUAL, token.getType())
        assertEquals(">=", token.getValue())
    }

    @Test
    fun `test LessThanToToken`() {
        assertTrue(LessThanToToken.canHandle("<"))
        assertFalse(LessThanToToken.canHandle("<="))

        val token = LessThanToToken.convert("<", position)
        assertEquals(TokenType.LESS_THAN, token.getType())
        assertEquals("<", token.getValue())
    }

    @Test
    fun `test LessThanOrEqualToToken`() {
        assertTrue(LessThanOrEqualToToken.canHandle("<="))
        assertFalse(LessThanOrEqualToToken.canHandle("<"))

        val token = LessThanOrEqualToToken.convert("<=", position)
        assertEquals(TokenType.LESS_THAN_OR_EQUAL, token.getType())
        assertEquals("<=", token.getValue())
    }

    @Test
    fun `test AndToToken`() {
        assertTrue(AndToToken.canHandle("&&"))
        assertFalse(AndToToken.canHandle("&"))

        val token = AndToToken.convert("&&", position)
        assertEquals(TokenType.AND, token.getType())
        assertEquals("&&", token.getValue())
    }

    @Test
    fun `test OrToToken`() {
        assertTrue(OrToToken.canHandle("||"))
        assertFalse(OrToToken.canHandle("|"))

        val token = OrToToken.convert("||", position)
        assertEquals(TokenType.OR, token.getType())
        assertEquals("||", token.getValue())
    }

    @Test
    fun `test NotToToken`() {
        assertTrue(NotToToken.canHandle("!"))
        assertFalse(NotToToken.canHandle("!="))

        val token = NotToToken.convert("!", position)
        assertEquals(TokenType.NOT, token.getType())
        assertEquals("!", token.getValue())
    }

    @Test
    fun `test ElseToToken`() {
        assertTrue(ElseToToken.canHandle("else"))
        assertFalse(ElseToToken.canHandle("if"))

        val token = ElseToToken.convert("else", position)
        assertEquals(TokenType.ELSE, token.getType())
        assertEquals("else", token.getValue())
    }

    @Test
    fun `test FunctionToToken`() {
        assertTrue(FunctionToToken.canHandle("function"))
        assertFalse(FunctionToToken.canHandle("func"))

        val token = FunctionToToken.convert("function", position)
        assertEquals(TokenType.FUNCTION, token.getType())
        assertEquals("function", token.getValue())
    }

    @Test
    fun `test ForToToken`() {
        assertTrue(ForToToken.canHandle("for"))
        assertFalse(ForToToken.canHandle("while"))

        val token = ForToToken.convert("for", position)
        assertEquals(TokenType.FOR, token.getType())
        assertEquals("for", token.getValue())
    }

    @Test
    fun `test IfToToken`() {
        assertTrue(IfToToken.canHandle("if"))
        assertFalse(IfToToken.canHandle("else"))

        val token = IfToToken.convert("if", position)
        assertEquals(TokenType.IF, token.getType())
        assertEquals("if", token.getValue())
    }

    @Test
    fun `test PrintlnToToken`() {
        assertTrue(PrintlnToToken.canHandle("println"))
        assertFalse(PrintlnToToken.canHandle("print"))

        val token = PrintlnToToken.convert("println", position)
        assertEquals(TokenType.PRINT, token.getType())
        assertEquals("println", token.getValue())
    }

    @Test
    fun `test ReturnToToken`() {
        assertTrue(ReturnToToken.canHandle("return"))
        assertFalse(ReturnToToken.canHandle("ret"))

        val token = ReturnToToken.convert("return", position)
        assertEquals(TokenType.RETURN, token.getType())
        assertEquals("return", token.getValue())
    }

    @Test
    fun `test WhileToToken`() {
        assertTrue(WhileToToken.canHandle("while"))
        assertFalse(WhileToToken.canHandle("for"))

        val token = WhileToToken.convert("while", position)
        assertEquals(TokenType.WHILE, token.getType())
        assertEquals("while", token.getValue())
    }

    @Test
    fun `test LetToToken`() {
        assertTrue(LetToToken.canHandle("let"))
        assertFalse(LetToToken.canHandle("var"))

        val token = LetToToken.convert("let", position)
        assertEquals(TokenType.LET, token.getType())
        assertEquals("let", token.getValue())
    }

    @Test
    fun `test NumberToToken`() {
        assertTrue(NumberToToken.canHandle("number"))
        assertFalse(NumberToToken.canHandle("string"))

        val token = NumberToToken.convert("number", position)
        assertEquals(TokenType.NUMBER, token.getType())
        assertEquals("number", token.getValue())
    }

    @Test
    fun `test StringToToken`() {
        assertTrue(StringToToken.canHandle("string"))
        assertFalse(StringToToken.canHandle("number"))

        val token = StringToToken.convert("string", position)
        assertEquals(TokenType.STRING, token.getType())
        assertEquals("string", token.getValue())
    }

    @Test
    fun `test NumberLiteralToToken`() {
        assertTrue(NumberLiteralToToken.canHandle("123"))
        assertTrue(NumberLiteralToToken.canHandle("123.45"))
        assertTrue(NumberLiteralToToken.canHandle("0"))
        assertFalse(NumberLiteralToToken.canHandle("abc"))
        assertFalse(NumberLiteralToToken.canHandle("123abc"))

        val intToken = NumberLiteralToToken.convert("123", position)
        assertEquals(TokenType.NUMBER_LITERAL, intToken.getType())
        assertEquals("123", intToken.getValue())

        val doubleToken = NumberLiteralToToken.convert("123.45", position)
        assertEquals(TokenType.NUMBER_LITERAL, doubleToken.getType())
        assertEquals("123.45", doubleToken.getValue())
    }

    @Test
    fun `test StringLiteralToToken`() {
        assertTrue(StringLiteralToToken.canHandle("'hello'"))
        assertTrue(StringLiteralToToken.canHandle("\"hello\""))
        assertTrue(StringLiteralToToken.canHandle("''"))
        assertTrue(StringLiteralToToken.canHandle("\"\""))
        assertFalse(StringLiteralToToken.canHandle("hello"))
        assertFalse(StringLiteralToToken.canHandle("'hello"))
        assertFalse(StringLiteralToToken.canHandle("hello'"))

        val singleQuoteToken = StringLiteralToToken.convert("'hello'", position)
        assertEquals(TokenType.STRING_LITERAL, singleQuoteToken.getType())
        assertEquals("'hello'", singleQuoteToken.getValue())

        val doubleQuoteToken = StringLiteralToToken.convert("\"hello\"", position)
        assertEquals(TokenType.STRING_LITERAL, doubleQuoteToken.getType())
        assertEquals("\"hello\"", doubleQuoteToken.getValue())
    }

    @Test
    fun `test TokenConverterRegistry`() {
        val converters = listOf(
            OpenParenthesisToToken,
            CloseParenthesisToToken,
            NumberLiteralToToken,
            StringLiteralToToken
        )
        val registry = TokenConverterRegistry(converters)

        // Test valid conversions
        val openParen = registry.convert("(", position)
        assertEquals(TokenType.OPEN_PARENTHESIS, openParen.getType())

        val number = registry.convert("123", position)
        assertEquals(TokenType.NUMBER_LITERAL, number.getType())

        // Test fallback to IDENTIFIER
        val identifier = registry.convert("someVariable", position)
        assertEquals(TokenType.IDENTIFIER, identifier.getType())
        assertEquals("someVariable", identifier.getValue())
    }
}