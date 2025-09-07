import result.InterpreterResult
import node.Program
import node.BinaryExpression
import node.IdentifierExpression
import node.LiteralExpression
import node.AssignmentStatement
import node.DeclarationStatement
import node.PrintStatement
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals
import type.CommonTypes
import coordinates.Position
import factory.DefaultInterpreterFactory
import node.ExpressionStatement
import node.IfStatement

class InterpreterTest {
    @Test
    fun `Basic Variable Declaration and Assignment`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 1\n Output: ")
        val case1 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "x",
                                        Position(2, 9),
                                    ),
                                ),
                        ),
                    ),
            )
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case1)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 1\n Output: 42", printed)
    }

    @Test
    fun `Variable Reassignment and Expression Evaluation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 2\n Output: ")
        val case2 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue = null,
                        ),
                        AssignmentStatement(
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(2, 1)),
                            value =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(2, 5),
                                    ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "x",
                                        Position(3, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case2)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 2\n Output: 42", printed)
    }

    @Test
    fun `Basic Arithmetics Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 3\n Output: ")
        val case3 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "y",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case3)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 3\n Output: 52", printed)
    }

    @Test
    fun `Basic Double Arithmetics Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 4\n Output: ")
        val case4 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42.2",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10.0",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "y",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case4)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 4\n Output: 52.2", printed)
    }

    @Test
    fun `Multiplication and Division Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 5\n Output: ")
        val case5 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "a", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "6",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "b", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "3",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "c", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "a",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "b",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "d", Position(4, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(4, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "c",
                                                Position(4, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(4, 19)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "2",
                                                Position(4, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "d",
                                        Position(5, 9),
                                    ),
                                ),
                        ),
                    ),
            )
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case5)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 5\n Output: 9", printed)
    }

    @Test
    fun `Combined Arithmetic Operations`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 6\n Output: ")
        val case6 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "8",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "2",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        BinaryExpression(
                                            left =
                                                IdentifierExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.IDENTIFIER,
                                                        "x",
                                                        Position(3, 17),
                                                    ),
                                                ),
                                            operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(3, 19)),
                                            right =
                                                IdentifierExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.IDENTIFIER,
                                                        "y",
                                                        Position(3, 21),
                                                    ),
                                                ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(3, 23)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "5",
                                                Position(3, 25),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case6)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 6\n Output: 20", printed)
    }

    @Test
    fun `Complex Arithmetic with Multiple Variables`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 7\n Output: ")
        val case7 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "a", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "b", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "5",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "c", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "2",
                                        Position(3, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "d", Position(4, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(4, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "a",
                                                Position(4, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(4, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "b",
                                                Position(4, 21),
                                            ),
                                        ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "e", Position(5, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(5, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "d",
                                                Position(5, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(5, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "c",
                                                Position(5, 21),
                                            ),
                                        ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "f", Position(6, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(6, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "e",
                                                Position(6, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(6, 19)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "2",
                                                Position(6, 21),
                                            ),
                                        ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "g", Position(7, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(7, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "f",
                                                Position(7, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "-", Position(7, 19)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "4",
                                                Position(7, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "g",
                                        Position(8, 9),
                                    ),
                                ),
                        ),
                    ),
            )
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case7)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 7\n Output: 22", printed)
    }

    @Test
    fun `Double Division Operation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 8\n Output: ")
        val case8 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "a", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "b", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "3",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "c", Position(4, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(4, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "a",
                                                Position(4, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(4, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "b",
                                                Position(4, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "c",
                                        Position(8, 9),
                                    ),
                                ),
                        ),
                    ),
            )
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case8)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 8\n Output: 3.3333333333333335", printed)
    }

    @Test
    fun `Basic Arithmetics Operations with Doubles`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 9\n Output: ")
        val case9 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42.0",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10.0",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "y",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case9)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 9\n Output: 52.0", printed)
    }

    @Test
    fun `Divide Two Numbers Operation with Doubles`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 10\n Output: ")
        val case10 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42.0",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10.0",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "y",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case10)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 10\n Output: 4.2", printed)
    }

    @Test
    fun `Multiplication Two Numbers Operation with Doubles`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 11\n Output: ")
        val case11 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42.0",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10.0",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "y",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case11)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 11\n Output: 420.0", printed)
    }

    @Test
    fun `Subtraction Two Numbers Operation with Doubles`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 12\n Output: ")
        val case12 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42.0",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "y", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10.0",
                                        Position(2, 17),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "z", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 8)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(3, 17),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "-", Position(3, 19)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "y",
                                                Position(3, 21),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "z",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case12)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 12\n Output: 32.0", printed)
    }

    @Test
    fun `String Concatenation with Numbers`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 13\n Output: ")
        val case13 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "text", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(1, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "Hello",
                                        Position(1, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "number", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 13)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(2, 22),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "result", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(3, 13)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "text",
                                                Position(3, 22),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 27)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "number",
                                                Position(3, 29),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "result",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case13)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 13\n Output: Hello42", printed)
    }

    @Test
    fun `Number Addition with String Concatenation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 14\n Output: ")
        val case14 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "num1", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10",
                                        Position(1, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "num2", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "5",
                                        Position(2, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "text", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(3, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        " equals ",
                                        Position(3, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "sum", Position(4, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(4, 10)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "num1",
                                                Position(4, 19),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(4, 24)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "num2",
                                                Position(4, 26),
                                            ),
                                        ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "result", Position(5, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(5, 13)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        BinaryExpression(
                                            left =
                                                IdentifierExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.IDENTIFIER,
                                                        "num1",
                                                        Position(5, 22),
                                                    ),
                                                ),
                                            operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(5, 27)),
                                            right =
                                                IdentifierExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.IDENTIFIER,
                                                        "text",
                                                        Position(5, 29),
                                                    ),
                                                ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(5, 34)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "sum",
                                                Position(5, 36),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "result",
                                        Position(6, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case14)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 14\n Output: 10 equals 15", printed)
    }

    @Test
    fun `String and Double Concatenation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 15\n Output: ")
        val case15 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "message", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(1, 14)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "Pi is approximately ",
                                        Position(1, 23),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "pi", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 9)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "3.14159",
                                        Position(2, 18),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "result", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(3, 13)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "message",
                                                Position(3, 22),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 30)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "pi",
                                                Position(3, 32),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "result",
                                        Position(4, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case15)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 15\n Output: Pi is approximately 3.14159", printed)
    }

    @Test
    fun `Complex Edge Cases Test`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 16\n Output: ")
        val case16 =
            Program(
                statements =
                    listOf(
                        // Test zero operations
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "zero", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "0",
                                        Position(1, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "five", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "5",
                                        Position(2, 20),
                                    ),
                                ),
                        ),
                        // Zero addition
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "zeroAdd", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 14)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "zero",
                                                Position(3, 23),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(3, 28)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "five",
                                                Position(3, 30),
                                            ),
                                        ),
                                ),
                        ),
                        // Zero multiplication
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "zeroMult", Position(4, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(4, 15)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "zero",
                                                Position(4, 24),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(4, 29)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "five",
                                                Position(4, 31),
                                            ),
                                        ),
                                ),
                        ),
                        // Negative numbers
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "negative", Position(5, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(5, 15)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "-10",
                                        Position(5, 24),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "negativeResult", Position(6, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(6, 21)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "negative",
                                                Position(6, 30),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(6, 39)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "five",
                                                Position(6, 41),
                                            ),
                                        ),
                                ),
                        ),
                        // Very small decimal
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "smallDecimal", Position(7, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(7, 19)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "0.001",
                                        Position(7, 28),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "precision", Position(8, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(8, 16)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "smallDecimal",
                                                Position(8, 25),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(8, 38)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "1000",
                                                Position(8, 40),
                                            ),
                                        ),
                                ),
                        ),
                        // Multiple variable reassignments
                        AssignmentStatement(
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "five", Position(9, 1)),
                            value =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "five",
                                                Position(9, 8),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(9, 13)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "2",
                                                Position(9, 15),
                                            ),
                                        ),
                                ),
                        ),
                        // Complex nested expression with mixed types
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "complexResult", Position(10, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(10, 20)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        BinaryExpression(
                                            left =
                                                BinaryExpression(
                                                    left =
                                                        LiteralExpression(
                                                            PrintScriptToken(
                                                                CommonTypes.STRING_LITERAL,
                                                                "Results: ",
                                                                Position(10, 29),
                                                            ),
                                                        ),
                                                    operator =
                                                        PrintScriptToken(
                                                            CommonTypes.OPERATORS,
                                                            "+",
                                                            Position(10, 40),
                                                        ),
                                                    right =
                                                        IdentifierExpression(
                                                            PrintScriptToken(
                                                                CommonTypes.IDENTIFIER,
                                                                "zeroAdd",
                                                                Position(10, 42),
                                                            ),
                                                        ),
                                                ),
                                            operator =
                                                PrintScriptToken(
                                                    CommonTypes.OPERATORS,
                                                    "+",
                                                    Position(10, 50),
                                                ),
                                            right =
                                                LiteralExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.STRING_LITERAL,
                                                        ", ",
                                                        Position(10, 52),
                                                    ),
                                                ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(10, 56)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "precision",
                                                Position(10, 58),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "complexResult",
                                        Position(11, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case16)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 16\n Output: Results: 5, 1.0", printed)
    }

    @Test
    fun `Empty String Concatenation`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 18\n Output: ")
        val case18 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "empty", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(1, 12)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "",
                                        Position(1, 21),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "number", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 13)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "123",
                                        Position(2, 22),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "text", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(3, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "Hello",
                                        Position(3, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "result1", Position(4, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(4, 14)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "empty",
                                                Position(4, 23),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(4, 29)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "number",
                                                Position(4, 31),
                                            ),
                                        ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "result2", Position(5, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(5, 14)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "text",
                                                Position(5, 23),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(5, 28)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "empty",
                                                Position(5, 30),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                BinaryExpression(
                                    left =
                                        BinaryExpression(
                                            left =
                                                IdentifierExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.IDENTIFIER,
                                                        "result1",
                                                        Position(6, 9),
                                                    ),
                                                ),
                                            operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(6, 17)),
                                            right =
                                                LiteralExpression(
                                                    PrintScriptToken(
                                                        CommonTypes.STRING_LITERAL,
                                                        " ",
                                                        Position(6, 19),
                                                    ),
                                                ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(6, 23)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "result2",
                                                Position(6, 25),
                                            ),
                                        ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case18)
        assertTrue(result.interpretedCorrectly)
        assertEquals(result.message, "Program executed successfully")
        val printed = outputStream.toString().trim()
        assertEquals("Program 18\n Output: 123 Hello", printed)
    }

    @Test
    fun `Empty Program Should Pass`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case = Program(statements = emptyList())

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case)

        assertTrue(result.interpretedCorrectly)
        assertEquals("Program executed successfully", result.message)
    }

    @Test
    fun `Complex Variable Delcaration and Reassignment Should Work`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 19\n Output: ")
        val case19 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                        AssignmentStatement(
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(2, 1)),
                            value =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "x",
                                                Position(2, 8),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(2, 10)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "5",
                                                Position(2, 12),
                                            ),
                                        ),
                                ),
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "x",
                                        Position(3, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case19)
        assertTrue(result.interpretedCorrectly)
        assertEquals("Program executed successfully", result.message)
        val printed = outputStream.toString().trim()
        assertEquals("Program 19\n Output: 15", printed)
    }

    @Test
    fun `test program with useless expression statements runs correctly`() {
        // --- 1. Preparación (Setup) ---

        // Capturamos la salida de la consola para poder verificarla
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // El nodo Program que creamos en el paso anterior
        val uselessExpressionsProgram =
            Program(
                statements =
                    listOf(
                        // let miVariable: number = 42;
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "miVariable", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 17)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(1, 26),
                                    ),
                                ),
                        ),
                        // "un string";
                        ExpressionStatement(
                            expression =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "un string",
                                        Position(2, 1),
                                    ),
                                ),
                        ),
                        // 123.45;
                        ExpressionStatement(
                            expression =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "123.45",
                                        Position(3, 1),
                                    ),
                                ),
                        ),
                        // 10 + 5;
                        ExpressionStatement(
                            expression =
                                BinaryExpression(
                                    left =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "10",
                                                Position(4, 1),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(4, 4)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "5",
                                                Position(4, 6),
                                            ),
                                        ),
                                ),
                        ),
                        // miVariable;
                        ExpressionStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "miVariable",
                                        Position(5, 1),
                                    ),
                                ),
                        ),
                        // println(miVariable);
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "miVariable",
                                        Position(6, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        // Asumimos que tienes una instancia del intérprete lista para usar
        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        // Le pasamos el AST completo al intérprete
        val result: InterpreterResult = interpreter.interpret(uselessExpressionsProgram)

        // --- 3. Verificación (Assertions) ---

        // Primero, verificamos que el intérprete terminó correctamente
        assertTrue(result.interpretedCorrectly, "El programa debería haberse ejecutado sin errores.")
        assertEquals("Program executed successfully", result.message)

        // Segundo, verificamos que la salida en consola es la esperada
        val printedOutput = outputStream.toString().trim() // .trim() para quitar saltos de línea
        assertEquals("42", printedOutput, "La única salida debería ser el valor de 'miVariable'.")
    }

    @Test
    fun `Printing Null Variable Should Succeed`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val programWithNullVar =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "myVar", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(1, 12)),
                            initialValue = null,
                        ),
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(CommonTypes.IDENTIFIER, "myVar", Position(2, 9)),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        val result: InterpreterResult = interpreter.interpret(programWithNullVar)

        assertTrue(result.interpretedCorrectly, "El programa debería ejecutarse correctamente.")

        val printedOutput = outputStream.toString().trim()
        assertEquals("null", printedOutput, "La salida de una variable null debería ser 'null'.")
    }

    @Test
    fun `Print Simple Sum Without Declaration Should Succeed`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        // This program represents the code: println(5 + 10);
        val simpleSumProgram =
            Program(
                statements =
                    listOf(
                        PrintStatement(
                            expression =
                                BinaryExpression(
                                    left =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "5",
                                                Position(1, 9),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "+", Position(1, 11)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(
                                                CommonTypes.NUMBER_LITERAL,
                                                "10",
                                                Position(1, 13),
                                            ),
                                        ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(simpleSumProgram)

        // Assert that the program executed without errors
        assertTrue(result.interpretedCorrectly, "The program should have executed successfully.")
        assertEquals("Program executed successfully", result.message)

        // Assert that the correct output was printed to the console
        val printedOutput = outputStream.toString().trim()
        assertEquals("15", printedOutput, "The output should be the result of the sum.")
    }

    @Test
    fun `Simple IfStatement Should Pass`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        /* if( true ){
            println("ENTRAAAA")
           }else {
                println("NO ENTRAAAA")
           }
        }
         * */

        val programWithStatement =
            Program(
                listOf(
                    IfStatement(
                        LiteralExpression(
                            PrintScriptToken(
                                CommonTypes.BOOLEAN_LITERAL,
                                "true",
                                Position(1, 1),
                            ),
                        ),
                        PrintStatement(
                            LiteralExpression(
                                PrintScriptToken(
                                    CommonTypes.STRING_LITERAL,
                                    "ENTRAAAAAAAAAAAAAAAA",
                                    Position(1, 1),
                                ),
                            ),
                        ),
                        PrintStatement(
                            LiteralExpression(
                                PrintScriptToken(
                                    CommonTypes.STRING_LITERAL,
                                    "NO ENTRAAAAAAAAAAAAAAAA",
                                    Position(1, 1),
                                ),
                            ),
                        ),
                    ),
                ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        val result: InterpreterResult = interpreter.interpret(programWithStatement)

        assertTrue(result.interpretedCorrectly, "El programa debería ejecutarse correctamente.")
        assertEquals("Program executed successfully", result.message)
        val printedOutput = outputStream.toString().trim()
        assertEquals("ENTRAAAAAAAAAAAAAAAA", printedOutput, "La salida debería ser 'ENTRAAAAAAAAAAAAAAAA'.")
    }

    @Test
    fun `Simple Or IfStatement Should Pass`() {
    }
}
