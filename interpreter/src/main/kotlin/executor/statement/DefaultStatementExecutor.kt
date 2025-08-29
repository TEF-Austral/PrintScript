package executor.statement

import executor.expression.DefaultExpressionExecutor
import executor.result.InterpreterResult
import node.AssignmentStatement
import node.DeclarationStatement
import node.ExpressionStatement
import node.PrintStatement
import node.Statement

class DefaultStatementExecutor: StatementExecutor {
    private val variables = mutableMapOf<String, Any>()
    private val defaultExpressionExecutor = DefaultExpressionExecutor(variables)

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            when (statement) {
                is DeclarationStatement -> {
                    val initialValue = statement.getInitialValue()
                    if (initialValue != null) {
                        val expressionResult = defaultExpressionExecutor.execute(initialValue)
                        if (!expressionResult.interpretedCorrectly) {
                            return expressionResult
                        }
                        val value = defaultExpressionExecutor.evaluate(initialValue)
                        variables[statement.getIdentifier()] = value
                    } else {
                        variables[statement.getIdentifier()] = getDefaultValue(statement.getDataType())
                    }
                    InterpreterResult(true, "Declaration executed successfully", null)
                }
                is AssignmentStatement -> {
                    val expressionResult = defaultExpressionExecutor.execute(statement.getValue())
                    if (!expressionResult.interpretedCorrectly) {
                        return expressionResult
                    }
                    val value = defaultExpressionExecutor.evaluate(statement.getValue())
                    variables[statement.getIdentifier()] = value
                    InterpreterResult(true, "Assignment executed successfully", null)
                }
                is PrintStatement -> {
                    val expressionResult = defaultExpressionExecutor.execute(statement.getExpression())
                    if (!expressionResult.interpretedCorrectly) {
                        return expressionResult
                    }
                    val value = defaultExpressionExecutor.evaluate(statement.getExpression())
                    println(value)
                    InterpreterResult(true, "Print executed successfully", null)
                }
                is ExpressionStatement -> {
                    val expressionResult = defaultExpressionExecutor.execute(statement.getExpression())
                    if (!expressionResult.interpretedCorrectly) {
                        return expressionResult
                    }
                    defaultExpressionExecutor.evaluate(statement.getExpression())
                    InterpreterResult(true, "Expression executed successfully", null)
                }
                else -> {
                    InterpreterResult(false, "Unknown statement type", null)
                }
            }
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing statement: ${e.message}", null)
        }
    }

    override fun canHandle(statement: Statement): Boolean {
        TODO("Not yet implemented")
    }

    private fun getDefaultValue(dataType: String): Any =
        when (dataType.lowercase()) {
            "string" -> ""
            "number", "int" -> 0
            else -> ""
        }
}
