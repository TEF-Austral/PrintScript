package executor

import node.ASTNode

class InOrderExecutor(private val executorRegistry: List<Executor>): Executor {
    override fun execute(node: ASTNode) {
        for (executor in executorRegistry) {
            executor.execute(node)
        }
    }
}