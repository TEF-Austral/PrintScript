import builder.DefaultNodeBuilder
import node.*
import node.expression.BinaryExpression
import node.expression.IdentifierExpression
import node.expression.LiteralExpression
import node.statement.DeclarationStatement
import node.statement.ExpressionStatement
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
        assertTrue(statement is DeclarationStatement)

        val varDecl = statement as DeclarationStatement
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
        assertTrue(firstStatement is DeclarationStatement)
        val firstVarDecl = firstStatement as DeclarationStatement
        assertEquals("x", firstVarDecl.getIdentifier())
        assertEquals("NUMBER", firstVarDecl.getDataType())
        assertEquals("5", (firstVarDecl.getInitialValue() as LiteralExpression).getValue())

        val secondStatement = program.getStatements()[1]
        assertTrue(secondStatement is DeclarationStatement)
        val secondVarDecl = secondStatement as DeclarationStatement
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

    @Test
    fun testComplexExpressionWithParenthesesAndPrecedence() {
        // Expresión: (x + 5) * 2 > 10
        val tokens = listOf(
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 1)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 2)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(1, 4)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(1, 6)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 7)),
            PrintScriptToken(TokenType.OPERATORS, "*", Position(1, 9)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 11)),
            PrintScriptToken(TokenType.COMPARISON, ">", Position(1, 13)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 15)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 17))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveDescentParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse()

        // Verificar que se parsea como un ExpressionStatement
        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is ExpressionStatement)

        val exprStatement = statement as ExpressionStatement
        val rootExpression = exprStatement.getExpression()
        assertTrue(rootExpression is BinaryExpression)

        // El AST debe ser: ((x + 5) * 2) > 10
        val rootBinary = rootExpression as BinaryExpression

        // Verificar operador raíz (>)
        assertEquals(">", rootBinary.getOperator().getValue())

        // Verificar operando derecho (10)
        assertTrue(rootBinary.getRight() is LiteralExpression)
        val rightLiteral = rootBinary.getRight() as LiteralExpression
        assertEquals("10", rightLiteral.getValue())

        // Verificar operando izquierdo ((x + 5) * 2)
        assertTrue(rootBinary.getLeft() is BinaryExpression)
        val leftMultiplication = rootBinary.getLeft() as BinaryExpression

        // Verificar multiplicación (*)
        assertEquals("*", leftMultiplication.getOperator().getValue())

        // Verificar operando derecho de la multiplicación (2)
        assertTrue(leftMultiplication.getRight() is LiteralExpression)
        val multiplicationRight = leftMultiplication.getRight() as LiteralExpression
        assertEquals("2", multiplicationRight.getValue())

        // Verificar operando izquierdo de la multiplicación (x + 5)
        assertTrue(leftMultiplication.getLeft() is BinaryExpression)
        val addition = leftMultiplication.getLeft() as BinaryExpression

        // Verificar suma (+)
        assertEquals("+", addition.getOperator().getValue())

        // Verificar operandos de la suma
        assertTrue(addition.getLeft() is IdentifierExpression)
        assertTrue(addition.getRight() is LiteralExpression)

        val identifier = addition.getLeft() as IdentifierExpression
        val additionRight = addition.getRight() as LiteralExpression

        assertEquals("x", identifier.getName())
        assertEquals("5", additionRight.getValue())
    }

    @Test
    fun testExpressionWithMultiplePrecedenceLevels() {
        // Expresión más simple para verificar precedencia: 2 + 3 * 4 > 10
        val tokens = listOf(
            PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 1)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(1, 3)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "3", Position(1, 5)),
            PrintScriptToken(TokenType.OPERATORS, "*", Position(1, 7)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "4", Position(1, 9)),
            PrintScriptToken(TokenType.COMPARISON, ">", Position(1, 11)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 14)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 16))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveDescentParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is ExpressionStatement)

        val exprStatement = statement as ExpressionStatement
        val rootExpression = exprStatement.getExpression()
        assertTrue(rootExpression is BinaryExpression)

        // El AST debe ser: (2 + (3 * 4)) > 10
        val rootBinary = rootExpression as BinaryExpression
        assertEquals(">", rootBinary.getOperator().getValue())

        // Verificar que el lado izquierdo es 2 + (3 * 4)
        assertTrue(rootBinary.getLeft() is BinaryExpression)
        val leftAddition = rootBinary.getLeft() as BinaryExpression
        assertEquals("+", leftAddition.getOperator().getValue())

        // Verificar que 3 * 4 se agrupa correctamente
        assertTrue(leftAddition.getRight() is BinaryExpression)
        val multiplication = leftAddition.getRight() as BinaryExpression
        assertEquals("*", multiplication.getOperator().getValue())

        // Verificar operandos finales
        val two = leftAddition.getLeft() as LiteralExpression
        val three = multiplication.getLeft() as LiteralExpression
        val four = multiplication.getRight() as LiteralExpression
        val ten = rootBinary.getRight() as LiteralExpression

        assertEquals("2", two.getValue())
        assertEquals("3", three.getValue())
        assertEquals("4", four.getValue())
        assertEquals("10", ten.getValue())
    }

    @Test
    fun testNestedParenthesesExpression() {
        // Expresión: ((x + 1) * (y - 2))
        val tokens = listOf(
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 1)),
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 2)),
            PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 3)),
            PrintScriptToken(TokenType.OPERATORS, "+", Position(1, 5)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "1", Position(1, 7)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 8)),
            PrintScriptToken(TokenType.OPERATORS, "*", Position(1, 10)),
            PrintScriptToken(TokenType.DELIMITERS, "(", Position(1, 12)),
            PrintScriptToken(TokenType.IDENTIFIER, "y", Position(1, 13)),
            PrintScriptToken(TokenType.OPERATORS, "-", Position(1, 15)),
            PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(1, 17)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 18)),
            PrintScriptToken(TokenType.DELIMITERS, ")", Position(1, 19)),
            PrintScriptToken(TokenType.DELIMITERS, ";", Position(1, 20))
        )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveDescentParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is ExpressionStatement)

        val exprStatement = statement as ExpressionStatement
        val rootExpression = exprStatement.getExpression()
        assertTrue(rootExpression is BinaryExpression)

        val rootBinary = rootExpression as BinaryExpression
        assertEquals("*", rootBinary.getOperator().getValue())

        // Verificar sub-expresiones
        assertTrue(rootBinary.getLeft() is BinaryExpression)
        assertTrue(rootBinary.getRight() is BinaryExpression)

        val leftAddition = rootBinary.getLeft() as BinaryExpression
        val rightSubtraction = rootBinary.getRight() as BinaryExpression

        assertEquals("+", leftAddition.getOperator().getValue())
        assertEquals("-", rightSubtraction.getOperator().getValue())

        // Verificar identificadores y literales
        val xIdentifier = leftAddition.getLeft() as IdentifierExpression
        val oneLiteral = leftAddition.getRight() as LiteralExpression
        val yIdentifier = rightSubtraction.getLeft() as IdentifierExpression
        val twoLiteral = rightSubtraction.getRight() as LiteralExpression

        assertEquals("x", xIdentifier.getName())
        assertEquals("1", oneLiteral.getValue())
        assertEquals("y", yIdentifier.getName())
        assertEquals("2", twoLiteral.getValue())
    }
}