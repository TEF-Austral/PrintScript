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
    private val database: DataBase = DefaultDataBase(),
    private val expression: DefaultExpressionExecutor,
    private val defaultStatementExecutor: DefaultStatementExecutor,
) : Interpreter {
    override fun interpret(node: ASTNode): InterpreterResult =
        try {
            when (node) {
                is Program -> handleProgram(node)
                is Statement -> defaultStatementExecutor.execute(node, database)
                is Expression -> expression.execute(node, database)
            }
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing in order: ${e.message}", null)
        }

    private fun handleProgram(program: Program): InterpreterResult {
        val statements = program.getStatements()
        return executeStatements(statements, 0)
    }

    private fun executeStatements(statements: List<Statement>, currentIndex: Int): InterpreterResult {
        if (currentIndex >= statements.size) {
            return InterpreterResult(true, "Program executed successfully",null, database)
        }

        val currentStatement = statements[currentIndex]
        val result = defaultStatementExecutor.execute(currentStatement, database)

        if (!result.interpretedCorrectly) {
            return result
        }

        result.updatedDatabase?.let { newDatabase ->
            if (currentIndex + 1 < statements.size) {
                val remainingStatements = statements.subList(currentIndex + 1, statements.size)
                val newProgram = Program(remainingStatements)
                val newInterpreter = DefaultInterpreter(newDatabase, expression, defaultStatementExecutor)
                return newInterpreter.interpret(newProgram)
            } else {
                return InterpreterResult(true, "Program executed successfully", null, newDatabase)
            }
        }
        return executeStatements(statements, currentIndex + 1)
    }
}