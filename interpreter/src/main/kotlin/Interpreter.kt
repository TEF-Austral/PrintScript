import node.ASTNode
import result.InterpreterResult

interface Interpreter {
    fun interpret(node: ASTNode): InterpreterResult
}
