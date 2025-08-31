package executor.expression

import executor.result.InterpreterResult
import node.EmptyExpression
import node.Expression
import type.CommonTypes
import variable.Variable

class EmptyExpressionExecutor : SpecificExpressionExecutor {
    override fun canHandle(expression: Expression): Boolean = expression is EmptyExpression

    override fun execute(expression: Expression): InterpreterResult =
        try {
            InterpreterResult(true, "Empty expression evaluated", Variable(CommonTypes.EMPTY, ""))
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing empty expression: ${e.message}", null)
        }
}
