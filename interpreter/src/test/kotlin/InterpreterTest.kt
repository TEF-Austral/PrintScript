import executor.ExpressionExecutor
import executor.InOrderExecutor
import executor.StatementExecutor
import node.Program
import node.expression.BinaryExpression
import node.expression.IdentifierExpression
import node.expression.LiteralExpression
import node.statement.AssignmentStatement
import node.statement.DeclarationStatement
import node.statement.PrintStatement
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals

class InterpreterTest {

    @Test
    fun `Basic Variable Declaration and Assignment`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 1\n Output: ")
        val case1 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "42", Position(1, 17)))
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "x", Position(2, 9)))
                )
            )
        )
        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case1)

        val printed = outputStream.toString().trim()
        assertEquals("Program 1\n Output: 42", printed)
    }

    @Test
    fun `Variable Reassignment and Expression Evaluation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 2\n Output: ")
        val case2 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = null
                ),
                AssignmentStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "x", Position(2, 1)),
                    value = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "42", Position(2, 5)))
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "x", Position(3, 9)))
                )
            )
        )

        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case2)

        val printed = outputStream.toString().trim()
        assertEquals("Program 2\n Output: 42", printed)
    }

    @Test
    fun `Basic Arithmetics Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 3\n Output: ")
        val case3 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "42", Position(1, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "y", Position(2, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(2, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(2, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "z", Position(3, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(3, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "x", Position(3, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "+", Position(3, 19)),
                        right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "y", Position(3, 21)))
                    )
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "z", Position(4, 9)))
                )
            )
        )

        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case3)

        val printed = outputStream.toString().trim()
        assertEquals("Program 3\n Output: 52", printed)
    }

    @Test
    fun `Basic Double Arithmetics Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 4\n Output: ")
        val case3 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "42.2", Position(1, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "y", Position(2, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(2, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "10.0", Position(2, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "z", Position(3, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(3, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "x", Position(3, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "+", Position(3, 19)),
                        right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "y", Position(3, 21)))
                    )
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "z", Position(4, 9)))
                )
            )
        )

        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case3)

        val printed = outputStream.toString().trim()
        assertEquals("Program 4\n Output: 52.2", printed)
    }

    @Test
    fun `Multiplication and Division Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 5\n Output: ")
        val case4 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "a", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "6", Position(1, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "b", Position(2, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(2, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "3", Position(2, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "c", Position(3, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(3, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "a", Position(3, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "*", Position(3, 19)),
                        right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "b", Position(3, 21)))
                    )
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "d", Position(4, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(4, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "c", Position(4, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "/", Position(4, 19)),
                        right = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(4, 21)))
                    )
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "d", Position(5, 9)))
                )
            )
        )
        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case4)

        val printed = outputStream.toString().trim()
        assertEquals("Program 5\n Output: 9", printed)
    }

    @Test
    fun `Combined Arithmetic Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 6\n Output: ")
        val case5 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "x", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "8", Position(1, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "y", Position(2, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(2, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(2, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "z", Position(3, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(3, 8)),
                    initialValue = BinaryExpression(
                        left = BinaryExpression(
                            left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "x", Position(3, 17))),
                            operator = PrintScriptToken(TokenType.OPERATORS, "/", Position(3, 19)),
                            right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "y", Position(3, 21)))
                        ),
                        operator = PrintScriptToken(TokenType.OPERATORS, "*", Position(3, 23)),
                        right = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(3, 25)))
                    )
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "z", Position(4, 9)))
                )
            )
        )
        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case5)

        val printed = outputStream.toString().trim()
        assertEquals("Program 6\n Output: 20", printed)
    }

    @Test
    fun `Complex Arithmetic with Multiple Variables`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 7\n Output: ")
        val case6 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "a", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "b", Position(2, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(2, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "5", Position(2, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "c", Position(3, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(3, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(3, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "d", Position(4, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(4, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "a", Position(4, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "*", Position(4, 19)),
                        right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "b", Position(4, 21)))
                    )
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "e", Position(5, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(5, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "d", Position(5, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "+", Position(5, 19)),
                        right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "c", Position(5, 21)))
                    )
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "f", Position(6, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(6, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "e", Position(6, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "/", Position(6, 19)),
                        right = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "2", Position(6, 21)))
                    )
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "g", Position(7, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(7, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "f", Position(7, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "-", Position(7, 19)),
                        right = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "4", Position(7, 21)))
                    )
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "g", Position(8, 9)))
                )
            )
        )
        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case6)

        val printed = outputStream.toString().trim()
        assertEquals("Program 7\n Output: 22", printed)
    }

    @Test
    fun `Double Division Operation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 8\n Output: ")
        val case6 = Program(
            statements = listOf(
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "a", Position(1, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(1, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "10", Position(1, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "b", Position(2, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(2, 8)),
                    initialValue = LiteralExpression(PrintScriptToken(TokenType.NUMBER_LITERAL, "3", Position(2, 17)))
                ),
                DeclarationStatement(
                    identifier = PrintScriptToken(TokenType.IDENTIFIER, "c", Position(4, 5)),
                    dataType = PrintScriptToken(TokenType.DATA_TYPES, "number", Position(4, 8)),
                    initialValue = BinaryExpression(
                        left = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "a", Position(4, 17))),
                        operator = PrintScriptToken(TokenType.OPERATORS, "/", Position(4, 19)),
                        right = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "b", Position(4, 21)))
                    )
                ),
                PrintStatement(
                    expression = IdentifierExpression(PrintScriptToken(TokenType.IDENTIFIER, "c", Position(8, 9)))
                )
            )
        )
        val interpreter = Interpreter(InOrderExecutor(ExpressionExecutor(), StatementExecutor()))
        interpreter.interpret(case6)

        val printed = outputStream.toString().trim()
        assertEquals("Program 8\n Output: 3.3333333333333335", printed)
    }
}