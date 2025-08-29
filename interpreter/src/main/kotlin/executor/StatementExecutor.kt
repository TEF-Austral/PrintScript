package executor

import executor.result.InterpreterResult
import node.ASTNode
import node.AssignmentStatement
import node.DeclarationStatement
import node.ExpressionStatement
import node.PrintStatement

class StatementExecutor : Executor {
    private val variables = mutableMapOf<String, Any>()
    private val expressionExecutor = ExpressionExecutor(variables)

    override fun execute(node: ASTNode): InterpreterResult {
        return try {
            when (node) {
                is DeclarationStatement -> {
                    val initialValue = node.getInitialValue()
                    if (initialValue != null) {
                        val expressionResult = expressionExecutor.execute(initialValue)
                        if (!expressionResult.interpretedCorrectly) {
                            return expressionResult
                        }
                        val value = expressionExecutor.evaluate(initialValue)
                        variables[node.getIdentifier()] = value
                    } else {
                        variables[node.getIdentifier()] = getDefaultValue(node.getDataType())
                    }
                    InterpreterResult(true, "Declaration executed successfully", null)
                }
                is AssignmentStatement -> {
                    val expressionResult = expressionExecutor.execute(node.getValue())
                    if (!expressionResult.interpretedCorrectly) {
                        return expressionResult
                    }
                    val value = expressionExecutor.evaluate(node.getValue())
                    variables[node.getIdentifier()] = value
                    InterpreterResult(true, "Assignment executed successfully", null)
                }
                is PrintStatement -> {
                    val expressionResult = expressionExecutor.execute(node.getExpression())
                    if (!expressionResult.interpretedCorrectly) {
                        return expressionResult
                    }
                    val value = expressionExecutor.evaluate(node.getExpression())
                    println(value)
                    InterpreterResult(true, "Print executed successfully", null)
                }
                is ExpressionStatement -> {
                    val expressionResult = expressionExecutor.execute(node.getExpression())
                    if (!expressionResult.interpretedCorrectly) {
                        return expressionResult
                    }
                    expressionExecutor.evaluate(node.getExpression())
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

    private fun getDefaultValue(dataType: String): Any =
        when (dataType.lowercase()) {
            "string" -> ""
            "number", "int" -> 0
            else -> ""
        }
}
