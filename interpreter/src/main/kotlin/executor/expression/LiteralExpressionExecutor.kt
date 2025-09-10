package executor.expression

import result.InterpreterResult
import node.Expression
import node.LiteralExpression
import variable.Variable

class LiteralExpressionExecutor : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is LiteralExpression

    override fun execute(expression: Expression): InterpreterResult =
        try {
            expression as LiteralExpression
            val type = expression.getType()
            val value = expression.getValue()
            InterpreterResult(true, "Literal evaluated", Variable(type, value))
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing literal expression: ${e.message}", null)
        }
}
