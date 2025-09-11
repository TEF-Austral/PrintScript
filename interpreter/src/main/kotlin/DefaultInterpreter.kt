import data.DataBase
import data.DefaultDataBase
import executor.expression.DefaultExpressionExecutor
import executor.statement.DefaultStatementExecutor
import result.InterpreterResult
import node.ASTNode
import node.Program
import node.Expression
import node.Statement

class DefaultInterpreter(
    private var database: DataBase = DefaultDataBase(),
    private val expression: DefaultExpressionExecutor,
    private val defaultStatementExecutor: DefaultStatementExecutor,
) : Interpreter {
    override fun interpret(node: ASTNode): InterpreterResult =
        try {
            when (node) {
                is Program -> handleProgram(node)
                is Statement -> {
                    val result = defaultStatementExecutor.execute(node, database)
                    result.updatedDatabase?.let { database = it }
                    result
                }
                is Expression -> expression.execute(node, database)
            }
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing in order: ${e.message}", null)
        }

    private fun handleProgram(program: Program): InterpreterResult {
        for (statement in program.getStatements()) {
            val result = interpret(statement)
            if (!result.interpretedCorrectly) {
                return result
            }
            result.updatedDatabase?.let { database = it }
        }
        return InterpreterResult(true, "Program executed successfully", null)
    }
}
