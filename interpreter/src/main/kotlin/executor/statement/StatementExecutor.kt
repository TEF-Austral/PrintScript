package executor.statement

import executor.result.InterpreterResult
import node.Statement

sealed interface StatementExecutor {
    fun execute(statement: Statement): InterpreterResult
}