package executor.expression

import node.Expression

sealed interface SpecificExpressionExecutor : ExpressionExecutor {
    fun canHandle(expression: Expression): Boolean
}
