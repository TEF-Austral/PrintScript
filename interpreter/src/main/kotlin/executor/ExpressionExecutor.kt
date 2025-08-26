package executor

import node.ASTNode
import node.expression.*
import executor.operators.Divide
import executor.operators.Multiplication
import executor.operators.Operator
import executor.operators.Subtraction
import executor.operators.Sum

class ExpressionExecutor(private val variables: MutableMap<String, Any> = mutableMapOf()) : Executor {
    val operators: List<Operator> = listOf(Sum, Divide, Multiplication, Subtraction)

    // TODO compsoite :)
    override fun execute(node: ASTNode) { evaluate(node as Expression) }

    fun evaluate(expression: Expression): Any {
        return when (expression) {
            is LiteralExpression -> { val value = expression.getValue()
                when {
                    value.toIntOrNull() != null -> value.toInt()
                    else -> value.removeSurrounding("\"")
                }
            }
            is IdentifierExpression -> { variables[expression.getName()] ?: "" }
            is BinaryExpression -> {
                val left = evaluate(expression.getLeft()).toString()
                val right = evaluate(expression.getRight()).toString()
                evaluateBinaryOperation(left, expression.getOperator().getValue(), right)
            }
            is EmptyExpression -> ""
        }
    }

    private fun evaluateBinaryOperation(left: String, sOperator: String, right: String): String {
        for (operator in operators) {
            if (operator.canHandle(sOperator)) { return operator.operate(left, right) }
        }
        return ""
    }
}