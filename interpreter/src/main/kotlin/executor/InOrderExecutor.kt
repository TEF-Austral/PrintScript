package executor

import node.ASTNode
import node.Program

class InOrderExecutor(private val executorRegistry: List<Executor>) : Executor {
    override fun execute(node: ASTNode) {
        when (node) {
            is Program -> {
                for (statement in node.getStatements()) {
                    execute(statement)
                }
            }
            else -> {
                for (executor in executorRegistry) {
                    executor.execute(node)
                }
            }
        }
    }
}