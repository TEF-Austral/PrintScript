package executor.expression

import result.InterpreterResult
import node.Expression
import node.IdentifierExpression
import variable.Variable

class IdentifierExpressionExecutor(
    private val variables: MutableMap<String, Variable>,
) : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is IdentifierExpression

    override fun execute(expression: Expression): InterpreterResult =
        try {
            expression as IdentifierExpression
            val variable = variables[expression.getValue()]
            if (variable == null) {
                InterpreterResult(false, "Identifier not found", null)
            } else {
                InterpreterResult(true, "Identifier evaluated", variable)
            }
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing identifier expression: ${e.message}", null)
        }
}
