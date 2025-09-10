package executor.expression

import result.InterpreterResult
import node.Expression

class DefaultExpressionExecutor(
    private val specificExecutors: List<SpecificExpressionExecutor>,
) : ExpressionExecutor {
    override fun execute(expression: Expression): InterpreterResult {
        for (executor in specificExecutors) {
            if (executor.canHandle(expression)) {
                return executor.execute(expression)
            }
        }
        return InterpreterResult(false, "No executor found for the given expression", null)
    }
}
