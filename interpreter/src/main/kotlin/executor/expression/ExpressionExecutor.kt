package executor.expression

import result.InterpreterResult
import node.Expression

sealed interface ExpressionExecutor {
    fun execute(expression: Expression): InterpreterResult
}
