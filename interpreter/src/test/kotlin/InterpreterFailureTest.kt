import coordinates.Position
import result.InterpreterResult
import factory.DefaultInterpreterFactory
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertEquals
import node.AssignmentStatement
import node.BinaryExpression
import node.DeclarationStatement
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import type.CommonTypes

class InterpreterFailureTest {
    @Test
    fun `Undefined Variable Usage Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case =
            Program(
                statements =
                    listOf(
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(
                                        CommonTypes.IDENTIFIER,
                                        "undefinedVar",
                                        Position(1, 9),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        val result: InterpreterResult = interpreter.interpret(case)

        assertFalse(result.interpretedCorrectly)
        assertTrue(
            result.message.contains("undefinedVar") ||
                result.message.contains("undefined") ||
                result.message.contains(
                    "not found",
                ),
        )
    }

    @Test
    fun `Assignment to Undeclared Variable Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case =
            Program(
                statements =
                    listOf(
                        AssignmentStatement(
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "undeclaredVar", Position(1, 1)),
                            value =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case)

        assertFalse(result.interpretedCorrectly)
        assertEquals("Error: Variable 'undeclaredVar' not declared", result.message)
    }

    @Test
    fun `Type Mismatch in Assignment Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "numberVar", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 16)),
                            initialValue = null,
                        ),
                        AssignmentStatement(
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "numberVar", Position(2, 1)),
                            value =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "not a number",
                                        Position(2, 13),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case)

        assertFalse(result.interpretedCorrectly)
        assertTrue(result.message.contains("type") || result.message.contains("mismatch") || result.message.contains("incompatible"))
    }

    @Test
    fun `Invalid Arithmetic Operation Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "text1", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(1, 12)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "hello",
                                        Position(1, 21),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "text2", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(2, 12)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "world",
                                        Position(2, 21),
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
                                                "text1",
                                                Position(3, 22),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(3, 28)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "text2",
                                                Position(3, 30),
                                            ),
                                        ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case)

        assertFalse(result.interpretedCorrectly)
        assertEquals("Type mismatch: Incompatible types for Multiplication operation", result.message)
    }

    @Test
    fun `String Subtraction Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "str1", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(1, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "hello",
                                        Position(1, 20),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "str2", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.STRING, "string", Position(2, 11)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.STRING_LITERAL,
                                        "world",
                                        Position(2, 20),
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
                                                "str1",
                                                Position(3, 22),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "-", Position(3, 27)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "str2",
                                                Position(3, 29),
                                            ),
                                        ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case)

        assertFalse(result.interpretedCorrectly)
        assertEquals("Type mismatch: Incompatible types for Substraction operation", result.message)
    }

    @Test
    fun `String Division Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val case =
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
                                        "hello",
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
                                        "2",
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
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(3, 27)),
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
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()
        val result: InterpreterResult = interpreter.interpret(case)

        assertFalse(result.interpretedCorrectly)
        assertEquals("Type mismatch: Incompatible types for division operator", result.message)
    }

    @Test
    fun `Invalid Declaration Program`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val programWithInvalidDeclaration =
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
                                        CommonTypes.STRING_LITERAL,
                                        "hola",
                                        Position(1, 17),
                                    ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        val result: InterpreterResult = interpreter.interpret(programWithInvalidDeclaration)

        assertFalse(result.interpretedCorrectly, "La interpretación debería fallar por un error de tipos.")

        val errorMessage = result.message.lowercase()
        assertTrue(
            errorMessage.contains("type") || errorMessage.contains("mismatch") || errorMessage.contains("incompatible"),
            "El mensaje de error debería indicar un problema de tipos. Mensaje recibido: '${result.message}'",
        )
    }

    @Test
    fun `Printing Undefined Variable Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val programWithUndefinedVar =
            Program(
                statements =
                    listOf(
                        PrintStatement(
                            expression =
                                IdentifierExpression(
                                    PrintScriptToken(CommonTypes.IDENTIFIER, "undeclaredVar", Position(1, 9)),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        val result: InterpreterResult = interpreter.interpret(programWithUndefinedVar)

        assertFalse(result.interpretedCorrectly, "El programa debería fallar al usar una variable no definida.")

        val errorMessage = result.message.lowercase()
        assertTrue(
            errorMessage.contains("undefined") || errorMessage.contains("not found") || errorMessage.contains("not declared"),
            "El mensaje de error debería indicar que la variable no fue encontrada. Mensaje: '${result.message}'",
        )
    }

    @Test
    fun `Multiplying String By Number Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val programWithInvalidMultiplication =
            Program(
                statements =
                    listOf(
                        PrintStatement(
                            expression =
                                BinaryExpression(
                                    left =
                                        LiteralExpression(
                                            PrintScriptToken(CommonTypes.STRING_LITERAL, "hola", Position(1, 9)),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "*", Position(1, 16)),
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                                        ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        val result: InterpreterResult = interpreter.interpret(programWithInvalidMultiplication)

        assertFalse(result.interpretedCorrectly, "El programa debería fallar al multiplicar un string y un número.")

        assertEquals("Type mismatch: Incompatible types for Multiplication operation", result.message)
    }

    @Test
    fun `Dividing String By Number Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        val programWithInvalidDivision =
            Program(
                statements =
                    listOf(
                        PrintStatement(
                            expression =
                                BinaryExpression(
                                    left =
                                        LiteralExpression(
                                            PrintScriptToken(CommonTypes.STRING_LITERAL, "hola", Position(1, 9)),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(1, 16)), // El operador ahora es '/'
                                    right =
                                        LiteralExpression(
                                            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "5", Position(1, 18)),
                                        ),
                                ),
                        ),
                    ),
            )

        val interpreter = DefaultInterpreterFactory.createDefaultInterpreter()

        // --- 2. Ejecución ---
        val result: InterpreterResult = interpreter.interpret(programWithInvalidDivision)

        // --- 3. Verificación ---
        // La interpretación DEBE fallar.
        assertFalse(result.interpretedCorrectly, "El programa debería fallar al dividir un string por un número.")

        val errorMessage = result.message.lowercase()
        print(errorMessage)
        assertEquals("Type mismatch: Incompatible types for division operator", result.message)
    }

    @Test
    fun `Division by Zero Edge Case Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        print("Program 17\n Output: ")
        val case17 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "numerator", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 16)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "10",
                                        Position(1, 25),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "denominator", Position(2, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(2, 18)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "0",
                                        Position(2, 27),
                                    ),
                                ),
                        ),
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.LET, "let", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "result", Position(3, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(3, 13)),
                            initialValue =
                                BinaryExpression(
                                    left =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "numerator",
                                                Position(3, 22),
                                            ),
                                        ),
                                    operator = PrintScriptToken(CommonTypes.OPERATORS, "/", Position(3, 32)),
                                    right =
                                        IdentifierExpression(
                                            PrintScriptToken(
                                                CommonTypes.IDENTIFIER,
                                                "denominator",
                                                Position(3, 34),
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
        val result: InterpreterResult = interpreter.interpret(case17)
        assertFalse(result.interpretedCorrectly)
        assertEquals("Can't divide by zero", result.message)
        val printed = outputStream.toString().trim()
        assertEquals("Program 17\n Output:", printed)
    }

    @Test
    fun `Const Assigment With No Value Should Fail`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        print("Program 3\n Output: ")
        val case2 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.CONST, "const", Position(1, 1)),
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
        assertFalse(result.interpretedCorrectly)
        assertEquals("Constant 'x' must be initialized with a value", result.message)
    }

    @Test
    fun `Const Assigment And Reasigment Value`() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        print("Program 3\n Output: ")
        val case2 =
            Program(
                statements =
                    listOf(
                        DeclarationStatement(
                            PrintScriptToken(CommonTypes.CONST, "const", Position(1, 1)),
                            identifier = PrintScriptToken(CommonTypes.IDENTIFIER, "x", Position(1, 5)),
                            dataType = PrintScriptToken(CommonTypes.NUMBER, "number", Position(1, 8)),
                            initialValue =
                                LiteralExpression(
                                    PrintScriptToken(
                                        CommonTypes.NUMBER_LITERAL,
                                        "42",
                                        Position(0, 0),
                                    ),
                                ),
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
        assertFalse(result.interpretedCorrectly)
        assertEquals("Error: Cannot reassign a value to a constant 'x'", result.message)
    }
}
