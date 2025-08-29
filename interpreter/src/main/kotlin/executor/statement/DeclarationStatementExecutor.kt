package executor.statement

import executor.expression.DefaultExpressionExecutor
import executor.result.InterpreterResult
import node.DeclarationStatement
import node.Statement

class DeclarationStatementExecutor(
    private val variables: MutableMap<String, Any>,
    private val defaultExpressionExecutor: DefaultExpressionExecutor
) : SpecificStatementExecutor {

    override fun canHandle(statement: Statement): Boolean {
        return statement is DeclarationStatement
    }

    override fun execute(statement: Statement): InterpreterResult {
        return try {
            val declarationStatement = statement as DeclarationStatement
            val initialValue = declarationStatement.getInitialValue()

            if (initialValue != null) {
                val expressionResult = defaultExpressionExecutor.execute(initialValue)
                if (!expressionResult.interpretedCorrectly) {
                    return expressionResult
                }
                val value = expressionResult.interpreter ?: ""
                variables[declarationStatement.getIdentifier()] = value
            } else {
                variables[declarationStatement.getIdentifier()] = getDefaultValue(declarationStatement.getDataType())
            }

            InterpreterResult(true, "Declaration executed successfully", null)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing declaration statement: ${e.message}", null)
        }
    }

    private fun getDefaultValue(dataType: String): Any =
        when (dataType.lowercase()) {
            "string" -> ""
            "number", "int" -> 0
            else -> ""
        }
}