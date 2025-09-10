package executor.expression

import node.Expression
import node.ReadEnvExpression
import result.InterpreterResult
import type.CommonTypes
import variable.Variable

class ReadEnvExpressionExecutor : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is ReadEnvExpression

    override fun execute(expression: Expression): InterpreterResult {
        val readEnvExpression = expression as ReadEnvExpression
        val envName = readEnvExpression.envName()

        val envValue = System.getenv(envName)

        if (envValue != null) {
            val resultVariable = Variable(CommonTypes.STRING, envValue)
            return InterpreterResult(
                true,
                "Successfully read environment variable '$envName'.",
                resultVariable,
            )
        } else {
            return InterpreterResult(
                false,
                "Error: Environment variable '$envName' not found.",
                null,
            )
        }
    }
}
