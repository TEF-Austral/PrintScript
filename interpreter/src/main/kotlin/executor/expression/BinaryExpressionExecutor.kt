package executor.expression

import executor.operators.Divide
import executor.operators.Multiplication
import executor.operators.Operator
import executor.operators.Subtraction
import executor.operators.Sum
import executor.result.InterpreterResult
import node.BinaryExpression
import node.Expression

class BinaryExpressionExecutor(
    private val operators: List<Operator> = listOf(Sum, Divide, Multiplication, Subtraction),
    private val expressions: List<SpecificExpressionExecutor>,
) : SpecificExpressionExecutor {

    override fun canHandle(expression: Expression): Boolean {
        return expression is BinaryExpression
    }

    override fun execute(expression: Expression): InterpreterResult {
        return try {
            val binaryExpr = expression as BinaryExpression
            executeBinaryExpression(binaryExpr)
        } catch (e: Exception) {
            createErrorResult("Error executing binary expression: ${e.message}")
        }
    }

    private fun executeBinaryExpression(expression: BinaryExpression): InterpreterResult {
        val leftResult = executeSubExpression(expression.getLeft())
        if (!leftResult.interpretedCorrectly) return leftResult

        val rightResult = executeSubExpression(expression.getRight())
        if (!rightResult.interpretedCorrectly) return rightResult

        return evaluateOperation(leftResult, rightResult, expression.getOperator().getValue())
    }

    private fun executeSubExpression(expression: Expression): InterpreterResult {
        val executor = findExecutorFor(expression)
        return executor?.execute(expression) ?: createErrorResult("No executor found for expression type")
    }

    private fun findExecutorFor(expression: Expression): SpecificExpressionExecutor? {
        return expressions.find { it.canHandle(expression) }
    }

    private fun evaluateOperation(
        leftResult: InterpreterResult,
        rightResult: InterpreterResult,
        operatorValue: String
    ): InterpreterResult {
        val left = leftResult.interpreter.toString()
        val right = rightResult.interpreter.toString()
        val result = performOperation(left, operatorValue, right)
        return InterpreterResult(true, "Binary expression evaluated", result)
    }

    private fun performOperation(left: String, operatorValue: String, right: String): String {
        val operator = operators.find { it.canHandle(operatorValue) }
        return operator?.operate(left, right) ?: ""
    }

    private fun createErrorResult(message: String): InterpreterResult {
        return InterpreterResult(false, message, null)
    }
}