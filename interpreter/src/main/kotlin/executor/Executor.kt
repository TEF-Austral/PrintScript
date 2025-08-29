package executor

import executor.result.InterpreterResult
import node.ASTNode

sealed interface Executor {
    fun execute(node: ASTNode): InterpreterResult
}
