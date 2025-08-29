package executor.expression

import executor.result.InterpreterResult
import node.Expression

sealed interface ExpressionExecutor {
    fun execute(expression: Expression): InterpreterResult
}
