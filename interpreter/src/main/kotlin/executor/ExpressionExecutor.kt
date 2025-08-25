package executor

import node.ASTNode
import node.expression.*

class ExpressionExecutor(private val variables: MutableMap<String, Any> = mutableMapOf()) : Executor {
    override fun execute(node: ASTNode) {
        evaluate(node as Expression)
    }

    fun evaluate(expression: Expression): Any {
        return when (expression) {
            is LiteralExpression -> {
                val value = expression.getValue()
                when {
                    value.toIntOrNull() != null -> value.toInt()
                    value.toBooleanStrictOrNull() != null -> value.toBoolean()
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

    private fun evaluateBinaryOperation(left: Any, operator: String, right: Any): Any {
        return when (operator) {
            "+" -> {
                when {
                    left is Int && right is Int -> left + right
                    else -> left.toString() + right.toString()
                }
            }
            "-" -> {
                if (left is Int && right is Int) left - right else 0
            }
            "*" -> {
                if (left is Int && right is Int) left * right else 0
            }
            "/" -> {
                if (left is Int && right is Int && right != 0) left / right else 0
            }
            "==" -> left == right
            "!=" -> left != right
            ">" -> {
                if (left is Int && right is Int) left > right else false
            }
            "<" -> {
                if (left is Int && right is Int) left < right else false
            }
            ">=" -> {
                if (left is Int && right is Int) left >= right else false
            }
            "<=" -> {
                if (left is Int && right is Int) left <= right else false
            }
            else -> ""
        }
    }
}