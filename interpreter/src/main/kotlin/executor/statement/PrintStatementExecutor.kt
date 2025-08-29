package executor.statement

import executor.expression.DefaultExpressionExecutor
import executor.result.InterpreterResult
import node.PrintStatement
import node.Statement

class PrintStatementExecutor(
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is PrintStatement

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val printStatement = statement as PrintStatement
            val expressionResult = defaultExpressionExecutor.execute(printStatement.getExpression())
            if (!expressionResult.interpretedCorrectly) {
                return expressionResult
            }

            val value = expressionResult.interpreter ?: ""
            println(value)
            InterpreterResult(true, "Print executed successfully", null)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing print statement: ${e.message}", null)
        }
    }
}
