package executor

import executor.operators.Divide
import executor.operators.Multiplication
import executor.operators.Operator
import executor.operators.Subtraction
import executor.operators.Sum
import executor.result.InterpreterResult
import node.ASTNode
import node.BinaryExpression
import node.EmptyExpression
import node.Expression
import node.IdentifierExpression
import node.LiteralExpression

class ExpressionExecutor(
    private val variables: MutableMap<String, Any> = mutableMapOf(),
) : Executor {
    val operators: List<Operator> = listOf(Sum, Divide, Multiplication, Subtraction)

    override fun execute(node: ASTNode): InterpreterResult {
        return try {
            evaluate(node as Expression)
            InterpreterResult(true, "Expression executed successfully", null)
        } catch (e: Exception) {
            InterpreterResult(false, "Error executing expression: ${e.message}", null)
        }
    }

    fun evaluate(expression: Expression): Any =
        when (expression) {
            is LiteralExpression -> {
                expression.getValue()
            }
            is IdentifierExpression -> {
                variables[expression.getName()] ?: ""
            }
            is BinaryExpression -> {
                val left: String = evaluate(expression.getLeft()).toString()
                val right: String = evaluate(expression.getRight()).toString()
                evaluateBinaryOperation(left, expression.getOperator().getValue(), right)
            }
            is EmptyExpression -> ""
        }

    private fun evaluateBinaryOperation(
        left: String,
        sOperator: String,
        right: String,
    ): String {
        for (operator in operators) {
            if (operator.canHandle(sOperator)) {
                return operator.operate(left, right)
            }
        }
        return ""
    }
}