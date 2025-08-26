package executor

import node.ASTNode
import node.Program
import node.statement.Statement

class InOrderExecutor(private val expression: ExpressionExecutor,
                      private val statementExecutor: StatementExecutor) : Executor {

    override fun execute(node: ASTNode) {
        when (node) {
            is Program -> { for (statement in node.getStatements()) { execute(statement) } }
            is Statement -> { statementExecutor.execute(node) }
            else -> { expression.execute(node) }
        }
    }
}