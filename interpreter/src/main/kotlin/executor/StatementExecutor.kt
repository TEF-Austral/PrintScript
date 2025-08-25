package executor

import node.ASTNode
import node.statement.*

class StatementExecutor : Executor {
    private val variables = mutableMapOf<String, Any>()
    private val expressionExecutor = ExpressionExecutor(variables)

    override fun execute(node: ASTNode) {
        when (node) {
            is DeclarationStatement -> {
                val initialValue = node.getInitialValue()
                if (initialValue != null) {
                    val value = expressionExecutor.evaluate(initialValue)
                    variables[node.getIdentifier()] = value
                } else {
                    variables[node.getIdentifier()] = getDefaultValue(node.getDataType())
                }
            }
            is AssignmentStatement -> {
                val value = expressionExecutor.evaluate(node.getValue())
                variables[node.getIdentifier()] = value
            }
            is PrintStatement -> {
                val value = expressionExecutor.evaluate(node.getExpression())
                println(value)
            }
            is ExpressionStatement -> {
                expressionExecutor.evaluate(node.getExpression())
            }
        }
    }

    private fun getDefaultValue(dataType: String): Any {
        return when (dataType.lowercase()) {
            "string" -> ""
            "number", "int" -> 0
            "boolean" -> false
            else -> ""
        }
    }
}