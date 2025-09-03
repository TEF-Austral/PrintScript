import builder.DefaultNodeBuilder
import coordinates.Position
import node.BinaryExpression
import node.DeclarationStatement
import node.EmptyExpression
import node.ExpressionStatement
import node.IdentifierExpression
import node.LiteralExpression
import parser.factory.RecursiveParserFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import type.CommonTypes

class ParserTest {
    @Test
    fun testSingleVariableDeclaration() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse().getProgram()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is DeclarationStatement)

        val varDecl = statement as DeclarationStatement
        assertEquals("x", varDecl.getIdentifier())
        assertEquals(CommonTypes.NUMBER, varDecl.getDataType())
        assertTrue(varDecl.getInitialValue() is LiteralExpression)

        val literal = varDecl.getInitialValue() as LiteralExpression
        assertEquals("5", literal.getValue())
    }

    @Test
    fun testMultipleStatementsWithBinaryExpression() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(1, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 19)),
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(2, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(2, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(2, 9)),
                PrintScriptToken(CommonTypes.ASSIGNMENT, "=", Position(2, 16)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(2, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(2, 19)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(3, 1)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 2)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(3, 3)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(3, 4)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse().getProgram()

        assertEquals(3, program.getStatements().size)

        val firstStatement = program.getStatements()[0]
        assertTrue(firstStatement is DeclarationStatement)
        val firstVarDecl = firstStatement as DeclarationStatement
        assertEquals("x", firstVarDecl.getIdentifier())
        assertEquals(CommonTypes.NUMBER, firstVarDecl.getDataType())
        assertEquals("5", (firstVarDecl.getInitialValue() as LiteralExpression).getValue())

        val secondStatement = program.getStatements()[1]
        assertTrue(secondStatement is DeclarationStatement)
        val secondVarDecl = secondStatement as DeclarationStatement
        assertEquals("y", secondVarDecl.getIdentifier())
        assertEquals(CommonTypes.NUMBER, secondVarDecl.getDataType())
        assertEquals("5", (secondVarDecl.getInitialValue() as LiteralExpression).getValue())

        val thirdStatement = program.getStatements()[2]
        assertTrue(thirdStatement is ExpressionStatement)
        val exprStatement = thirdStatement as ExpressionStatement
        assertTrue(exprStatement.getExpression() is BinaryExpression)

        val binaryExpr = exprStatement.getExpression() as BinaryExpression
        assertTrue(binaryExpr.getLeft() is IdentifierExpression)
        assertTrue(binaryExpr.getRight() is IdentifierExpression)
        assertEquals("+", binaryExpr.getOperator())

        val leftIdentifier = binaryExpr.getLeft() as IdentifierExpression
        val rightIdentifier = binaryExpr.getRight() as IdentifierExpression
        assertEquals("x", leftIdentifier.getName())
        assertEquals("y", rightIdentifier.getName())
    }

    @Test
    fun testComplexExpressionWithParenthesesAndPrecedence() {
        // Expresión: (x + 5) * 2 > 10;
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 2)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 4)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 6)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 7)),
                PrintScriptToken(CommonTypes.OPERATORS, "*", Position(1, 9)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "2", Position(1, 11)),
                PrintScriptToken(CommonTypes.COMPARISON, ">", Position(1, 13)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "10", Position(1, 15)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 17)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse().getProgram()

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
        assertEquals(">", rootBinary.getOperator())

        // Verificar operando derecho (10)
        assertTrue(rootBinary.getRight() is LiteralExpression)
        val rightLiteral = rootBinary.getRight() as LiteralExpression
        assertEquals("10", rightLiteral.getValue())

        // Verificar operando izquierdo ((x + 5) * 2)
        assertTrue(rootBinary.getLeft() is BinaryExpression)
        val leftMultiplication = rootBinary.getLeft() as BinaryExpression

        // Verificar multiplicación (*)
        assertEquals("*", leftMultiplication.getOperator())

        // Verificar operando derecho de la multiplicación (2)
        assertTrue(leftMultiplication.getRight() is LiteralExpression)
        val multiplicationRight = leftMultiplication.getRight() as LiteralExpression
        assertEquals("2", multiplicationRight.getValue())

        // Verificar operando izquierdo de la multiplicación (x + 5)
        assertTrue(leftMultiplication.getLeft() is BinaryExpression)
        val addition = leftMultiplication.getLeft() as BinaryExpression

        // Verificar suma (+)
        assertEquals("+", addition.getOperator())

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
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "2", Position(1, 1)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 3)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 5)),
                PrintScriptToken(CommonTypes.OPERATORS, "*", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "4", Position(1, 9)),
                PrintScriptToken(CommonTypes.COMPARISON, ">", Position(1, 11)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "10", Position(1, 14)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 16)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()
        val program = parser.parse().getProgram()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is ExpressionStatement)

        val exprStatement = statement as ExpressionStatement
        val rootExpression = exprStatement.getExpression()
        assertTrue(rootExpression is BinaryExpression)

        // El AST debe ser: (2 + (3 * 4)) > 10
        val rootBinary = rootExpression as BinaryExpression
        assertEquals(">", rootBinary.getOperator())

        // Verificar que el lado izquierdo es 2 + (3 * 4)
        assertTrue(rootBinary.getLeft() is BinaryExpression)
        val leftAddition = rootBinary.getLeft() as BinaryExpression
        assertEquals("+", leftAddition.getOperator())

        // Verificar que 3 * 4 se agrupa correctamente
        assertTrue(leftAddition.getRight() is BinaryExpression)
        val multiplication = leftAddition.getRight() as BinaryExpression
        assertEquals("*", multiplication.getOperator())

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
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 1)),
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 2)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 3)),
                PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 5)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "1", Position(1, 7)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 8)),
                PrintScriptToken(CommonTypes.OPERATORS, "*", Position(1, 10)),
                PrintScriptToken(CommonTypes.DELIMITERS, "(", Position(1, 12)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(1, 13)),
                PrintScriptToken(CommonTypes.OPERATORS, "-", Position(1, 15)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "2", Position(1, 17)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 18)),
                PrintScriptToken(CommonTypes.DELIMITERS, ")", Position(1, 19)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 20)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse().getProgram()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is ExpressionStatement)

        val exprStatement = statement as ExpressionStatement
        val rootExpression = exprStatement.getExpression()
        assertTrue(rootExpression is BinaryExpression)

        val rootBinary = rootExpression as BinaryExpression
        assertEquals("*", rootBinary.getOperator())

        // Verificar sub-expresiones
        assertTrue(rootBinary.getLeft() is BinaryExpression)
        assertTrue(rootBinary.getRight() is BinaryExpression)

        val leftAddition = rootBinary.getLeft() as BinaryExpression
        val rightSubtraction = rootBinary.getRight() as BinaryExpression

        assertEquals("+", leftAddition.getOperator())
        assertEquals("-", rightSubtraction.getOperator())

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

    @Test
    fun simpleDeclaration() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.DECLARATION, "let", Position(1, 1)),
                PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                PrintScriptToken(CommonTypes.DELIMITERS, ":", Position(1, 7)),
                PrintScriptToken(CommonTypes.NUMBER, "NUMBER", Position(1, 9)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 16)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val program = parser.parse().getProgram()

        assertEquals(1, program.getStatements().size)
        val statement = program.getStatements()[0]
        assertTrue(statement is DeclarationStatement)

        val varDecl = statement as DeclarationStatement
        assertEquals("x", varDecl.getIdentifier())
        assertEquals(CommonTypes.NUMBER, varDecl.getDataType())
        assertTrue(varDecl.getInitialValue() is EmptyExpression)
    }

    @Test
    fun testInvalidExpressionMissingOperator() {
        val tokens =
            listOf(
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 1)),
                PrintScriptToken(CommonTypes.NUMBER_LITERAL, "3", Position(1, 3)),
                PrintScriptToken(CommonTypes.DELIMITERS, ";", Position(1, 4)),
            )

        val nodeBuilder = DefaultNodeBuilder()
        val parser = RecursiveParserFactory().createParser(tokens, nodeBuilder)
        val result = parser.parse()

        assertTrue(result.isSuccess())
        assertEquals( "Parsed successfully", result.message())
    }}
