import coordinates.Position
import executor.expression.ReadEnvExpressionExecutor
import node.ReadEnvExpression
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import type.CommonTypes

class ReadEnvTest {
    @Test
    fun `test read existing environment variable`() {
        val envVarName = "PATH"
        val expression = ReadEnvExpression(envVarName, Position(0, 0))
        val executor = ReadEnvExpressionExecutor()

        val result = executor.execute(expression)

        assertTrue(result.interpretedCorrectly)
        assertNotNull(result.interpreter)
        assertEquals(CommonTypes.STRING, result.interpreter?.getType())
        assertEquals(System.getenv(envVarName), result.interpreter?.getValue())
        assertEquals("Successfully read environment variable '$envVarName'.", result.message)
    }

    @Test
    fun `test read non-existing environment variable`() {
        val envVarName = "NON_EXISTENT_VARIABLE_FOR_TESTING_12345"
        val expression = ReadEnvExpression(envVarName, Position(0, 0))
        val executor = ReadEnvExpressionExecutor()

        val result = executor.execute(expression)

        assertFalse(result.interpretedCorrectly)
        assertNull(result.interpreter)
        assertEquals("Error: Environment variable '$envVarName' not found.", result.message)
    }
}
