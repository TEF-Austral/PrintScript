package executor.statement

import executor.expression.DefaultExpressionExecutor
import executor.result.InterpreterResult
import node.ExpressionStatement
import node.Statement

class ExpressionStatementExecutor(
    private val defaultExpressionExecutor: DefaultExpressionExecutor,
) : SpecificStatementExecutor {
    override fun canHandle(statement: Statement): Boolean = statement is ExpressionStatement

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val expressionStatement = statement as ExpressionStatement
            val expressionResult = defaultExpressionExecutor.execute(expressionStatement.getExpression())
            if (!expressionResult.interpretedCorrectly) {
                return expressionResult
            }
            InterpreterResult(true, "Expression executed successfully", null)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing expression statement: ${e.message}", null)
        }
    }
}
