package executor.expression

import executor.result.InterpreterResult
import node.EmptyExpression
import node.Expression

class EmptyExpressionExecutor : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is EmptyExpression

    override fun execute(expression: Expression): InterpreterResult =
        try {
            InterpreterResult(true, "Empty expression evaluated", "")
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing empty expression: ${e.message}", null)
        }
}
