package executor.statement

import node.Statement

sealed interface  SpecificStatementExecutor: StatementExecutor {
    fun canHandle(statement: Statement): Boolean
}