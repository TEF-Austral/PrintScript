package executor.statement

import result.InterpreterResult
import node.Statement

sealed interface StatementExecutor {
    fun execute(statement: Statement): InterpreterResult
}
