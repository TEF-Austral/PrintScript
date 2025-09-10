package executor.statement

import result.InterpreterResult
import node.Statement

class DefaultStatementExecutor(
    private val specificExecutors: List<SpecificStatementExecutor>,
) : StatementExecutor {
    override fun execute(statement: Statement): InterpreterResult {
        for (executor in specificExecutors) {
            if (executor.canHandle(statement)) {
                return executor.execute(statement)
            }
        }
        return InterpreterResult(false, "No executor found for the given statement", null)
    }
}
