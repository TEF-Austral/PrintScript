import coordinates.Coordinates
import coordinates.Position
import data.DataBase
import emitter.Emitter
import executor.expression.SpecificExpressionExecutor
import executor.statement.SpecificStatementExecutor
import factory.DefaultInterpreterFactory
import node.Expression
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program
import node.Statement
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import result.InterpreterResult
import type.CommonTypes
import type.Version
import variable.Variable

class FactoryTest {

    private val identifierToken = PrintScriptToken(CommonTypes.IDENTIFIER, "a", Position(1, 1))
    private val identifierExpression = IdentifierExpression(identifierToken, Position(1, 1))
    private val literalNumberExpression =
        LiteralExpression(
            PrintScriptToken(CommonTypes.NUMBER_LITERAL, "42", Position(1, 1)),
            Position(1, 1),
        )
    private val literalStringExpression =
        LiteralExpression(
            PrintScriptToken(CommonTypes.STRING_LITERAL, "\"hola\"", Position(1, 1)),
            Position(1, 1),
        )

    private class TestEmitter : Emitter {
        val messages = mutableListOf<String>()

        override fun emit(value: InterpreterResult) {
            messages.add(value.message)
        }

        override fun stringEmit(value: String) {
            messages.add(value)
        }
    }

     private class dummyDataBase : DataBase {
        private val variables = mutableMapOf<String, Variable>()
        private val constants = mutableMapOf<String, Variable>()

        override fun getVariables(): Map<String, Variable> {
            return variables
        }

        override fun getConstants(): Map<String, Variable> {
            return constants
        }

        override fun addVariable(key: String, value: Variable): DataBase {
            variables[key] = value
            return this
        }

        override fun addConstant(key: String, value: Variable): DataBase {
            constants[key] = value
            return this
        }

        override fun changeVariableValue(key: String, value: Variable): DataBase {
            if (variables.containsKey(key)) {
                variables[key] = value
            }
            return this
        }

        override fun getVariableValue(key: String): Any? {
            return variables[key]?.getValue()
        }

        override fun getConstantValue(key: String): Any? {
            return constants[key]?.getValue()
        }

        override fun isConstant(key: String): Boolean {
            return constants.containsKey(key)
        }

        override fun getValue(key: String): Any? {
            return variables[key]?.getValue() ?: constants[key]?.getValue()
        }
    }

    private class MockStatementExecutor : SpecificStatementExecutor {
        var executed = false

        override fun canHandle(statement: Statement): Boolean = true

        override fun execute(statement: Statement, database: DataBase): InterpreterResult {
            executed = true
            return InterpreterResult(true, "Mock statement executed successfully", null)
        }
    }

    private class MockExpressionExecutor : SpecificExpressionExecutor {
        var executed = false

        override fun canHandle(expression: Expression): Boolean = true

        override fun execute(expression: Expression, database: DataBase): InterpreterResult {
            executed = true
            return InterpreterResult(true, "Mock expression executed successfully", null)
        }
    }

    class DummyStatement : Statement {
        override fun getCoordinates(): Coordinates = Position(0, 0)
    }

    class DummyExpression : Expression {
        override fun getCoordinates(): Coordinates = Position(0, 0)
    }

    @Test
    fun `createWithVersionAndEmitter for version 1_0 should create a working interpreter`() {
        val factory = DefaultInterpreterFactory()
        val emitter = TestEmitter()
        val interpreter = factory.createWithVersionAndEmitter(Version.VERSION_1_0, emitter)
        val printStatement = PrintStatement(literalStringExpression, Position(1, 1))
        val program = Program(listOf(printStatement))

        val result = interpreter.interpret(program)

        assertTrue(result.interpretedCorrectly)
        assertEquals(1, emitter.messages.size)
        assertEquals("Print executed successfully", emitter.messages[0])
    }

    @Test
    fun `createWithVersionAndEmitter for version 1_1 should create a working interpreter`() {
        val factory = DefaultInterpreterFactory()
        val emitter = TestEmitter()
        val interpreter = factory.createWithVersionAndEmitter(Version.VERSION_1_1, emitter)
        val printStatement = PrintStatement(literalStringExpression, Position(1, 1))
        val program = Program(listOf(printStatement))

        val result = interpreter.interpret(program)

        assertTrue(result.interpretedCorrectly)
        assertEquals(1, emitter.messages.size)
        assertEquals("Print executed successfully", emitter.messages[0])
    }

    @Test
    fun `createCustomInterpreter should use provided executors`() {
        val factory = DefaultInterpreterFactory()
        val mockStatementExecutor = MockStatementExecutor()
        val mockExpressionExecutor = MockExpressionExecutor()
        val dummyStatement = DummyStatement()
        val dummyExpression = DummyExpression()

        val interpreter =
            factory.createCustomInterpreter(
                listOf(mockExpressionExecutor),
                listOf(mockStatementExecutor),
                TestEmitter(),
                database = dummyDataBase(),
            )

        interpreter.interpret(dummyStatement)
        interpreter.interpret(dummyExpression)

        assertTrue(mockStatementExecutor.executed)
        assertTrue(mockExpressionExecutor.executed)
    }
}
