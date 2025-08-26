package executor

import node.ASTNode
import node.expression.*
import executor.operators.Divide
import executor.operators.Equals
import executor.operators.GreaterThan
import executor.operators.GreaterThanOrEqual
import executor.operators.LessThan
import executor.operators.LessThanOrEqual
import executor.operators.Multiplication
import executor.operators.NotEquals
import executor.operators.Operator
import executor.operators.Subtraction
import executor.operators.Sum

class ExpressionExecutor(private val variables: MutableMap<String, Any> = mutableMapOf()) : Executor {

    val operators: List<Operator> = listOf(
        Sum, Divide, Multiplication, Subtraction, Equals, NotEquals,
        GreaterThan, LessThan, GreaterThanOrEqual, LessThanOrEqual)

    override fun execute(node: ASTNode) {
        evaluate(node as Expression) // TODO SACAR EL CAST BOLIVIANO
    }

    fun evaluate(expression: Expression): Any {
        return when (expression) {
            is LiteralExpression -> {
                val value = expression.getValue()
                when {
                    value.toIntOrNull() != null -> value.toInt()
                    else -> value.removeSurrounding("\"")
                }
            }
            is IdentifierExpression -> {
                variables[expression.getName()] ?: ""
            }
            is BinaryExpression -> {
                val left = evaluate(expression.getLeft())
                val right = evaluate(expression.getRight())
                evaluateBinaryOperation(left, expression.getOperator().getValue(), right)
            }
            is EmptyExpression -> ""
        }
    }

    private fun evaluateBinaryOperation(left: Any, sOperator: String, right: Any): Any {
        for (operator in operators) {
            if (operator.canHandle(sOperator)) {
                return operator.operate(left, right)
            }
        }
        return ""
    }
}