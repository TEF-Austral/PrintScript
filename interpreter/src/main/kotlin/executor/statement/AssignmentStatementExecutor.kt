package executor.statement

import executor.expression.DefaultExpressionExecutor
import executor.result.InterpreterResult
import node.AssignmentStatement
import node.Statement

class AssignmentStatementExecutor(
    private val variables: MutableMap<String, Any>,
    private val defaultExpressionExecutor: DefaultExpressionExecutor
) : SpecificStatementExecutor {

    override fun canHandle(statement: Statement): Boolean {
        return statement is AssignmentStatement
    }

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val assignmentStatement = statement as AssignmentStatement
            val expressionResult = defaultExpressionExecutor.execute(assignmentStatement.getValue())
            if (!expressionResult.interpretedCorrectly) {
                return expressionResult
            }

            val value = expressionResult.interpreter ?: ""
            variables[assignmentStatement.getIdentifier()] = value
            InterpreterResult(true, "Assignment executed successfully", null)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing assignment statement: ${e.message}", null)
        }
    }
}