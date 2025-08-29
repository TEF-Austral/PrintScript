package executor.expression

import executor.result.InterpreterResult
import node.Expression
import node.LiteralExpression

class LiteralExpressionExecutor: SpecificExpressionExecutor {

    override fun canHandle(expression: Expression): Boolean {
        return expression is LiteralExpression
    }

    override fun execute(expression: Expression): InterpreterResult {
        return try {
            expression as LiteralExpression
            InterpreterResult(true, "Literal evaluated", expression.getValue())
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing literal expression: ${e.message}", null)
        }
    }
}