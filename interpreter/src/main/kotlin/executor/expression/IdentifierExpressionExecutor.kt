package executor.expression

import executor.result.InterpreterResult
import node.Expression
import node.IdentifierExpression

class IdentifierExpressionExecutor(
    private val variables: MutableMap<String, Any>,
) : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is IdentifierExpression

    override fun execute(expression: Expression): InterpreterResult =
        try {
            expression as IdentifierExpression
            val value = variables[expression.getName()] ?: ""
            InterpreterResult(true, "Identifier evaluated", value)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing identifier expression: ${e.message}", null)
        }
}
