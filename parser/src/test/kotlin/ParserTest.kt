import builder.DefaultNodeBuilder
import node.*
import parser.factory.RecursiveDescentParserFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ParserTest {

    @Test
    fun testSingleVariableDeclaration() {
        val tokens = listOf(
            PrintScriptToken(TokenType.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(TokenType.DELIMITERS, ":", Position(1, 7)),
            PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(1, 9)),
            PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 19))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveDescentParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is VariableDeclarationStatement)

        val varDecl = statement as VariableDeclarationStatement
        assertEquals("x", varDecl.getIdentifier())
        assertEquals("NUMBER", varDecl.getDataType())
        assertTrue(varDecl.getInitialValue() is LiteralExpression)

        val literal = varDecl.getInitialValue() as LiteralExpression
        assertEquals("5", literal.getValue())
    }

    @Test
    fun testMultipleStatementsWithBinaryExpression() {
        val tokens = listOf(
            PrintScriptToken(TokenType.DECLARATION, "let", Position(1, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
            PrintScriptToken(TokenType.DELIMITERS, ":", Position(1, 7)),
            PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(1, 9)),
            PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(1, 16)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 19)),

            PrintScriptToken(TokenType.DECLARATION, "let", Position(2, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "y", Position(2, 5)),
            PrintScriptToken(TokenType.DELIMITERS, ":", Position(2, 7)),
            PrintScriptToken(TokenType.DATA_TYPES, "NUMBER", Position(2, 9)),
            PrintScriptToken(TokenType.ASSIGNMENT, "=", Position(2, 16)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(2, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(2, 19)),

            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(3, 1)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(3, 2)),
            PrintScriptToken(TokenType.IDENTIFIER, "y", Position(3, 3)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(3, 4))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveDescentParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse()

        assertEquals(3, program.getStatements().size)

        val firstStatement = program.getStatements()[0]
        assertTrue(firstStatement is VariableDeclarationStatement)
        val firstVarDecl = firstStatement as VariableDeclarationStatement
        assertEquals("x", firstVarDecl.getIdentifier())
        assertEquals("NUMBER", firstVarDecl.getDataType())
        assertEquals("5", (firstVarDecl.getInitialValue() as LiteralExpression).getValue())

        val secondStatement = program.getStatements()[1]
        assertTrue(secondStatement is VariableDeclarationStatement)
        val secondVarDecl = secondStatement as VariableDeclarationStatement
        assertEquals("y", secondVarDecl.getIdentifier())
        assertEquals("NUMBER", secondVarDecl.getDataType())
        assertEquals("5", (secondVarDecl.getInitialValue() as LiteralExpression).getValue())

        val thirdStatement = program.getStatements()[2]
        assertTrue(thirdStatement is ExpressionStatement)
        val exprStatement = thirdStatement as ExpressionStatement
        assertTrue(exprStatement.getExpression() is BinaryExpression)

        val binaryExpr = exprStatement.getExpression() as BinaryExpression
        assertTrue(binaryExpr.getLeft() is IdentifierExpression)
        assertTrue(binaryExpr.getRight() is IdentifierExpression)
        assertEquals("+", binaryExpr.getOperator().getValue())

        val leftIdentifier = binaryExpr.getLeft() as IdentifierExpression
        val rightIdentifier = binaryExpr.getRight() as IdentifierExpression
        assertEquals("x", leftIdentifier.getName())
        assertEquals("y", rightIdentifier.getName())
    }
}