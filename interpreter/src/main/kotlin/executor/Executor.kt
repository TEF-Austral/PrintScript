package executor

import node.ASTNode

sealed interface Executor {
    fun execute(node: ASTNode)
}