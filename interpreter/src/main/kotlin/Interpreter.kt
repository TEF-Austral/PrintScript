import executor.expression.DefaultExpressionExecutor
import executor.statement.DefaultStatementExecutor
import executor.result.InterpreterResult
import node.ASTNode
import node.Program
import node.Expression
import node.Statement

class Interpreter(private val expression: DefaultExpressionExecutor, private val defaultStatementExecutor: DefaultStatementExecutor) {

    fun interpret(node: ASTNode): InterpreterResult {
        return try {
            when (node) {
                is Program -> handleProgram(node)
                is Statement -> defaultStatementExecutor.execute(node)
                is Expression -> expression.execute(node)
            }
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing in order: ${e.message}", null)
        }
    }

    private fun handleProgram(program: Program): InterpreterResult {
        for (statement in program.getStatements()) {
            val result = interpret(statement)
            if (!result.interpretedCorrectly) {
                return result
            }
        }
        return InterpreterResult(true, "Program executed successfully", null)
    }
}
