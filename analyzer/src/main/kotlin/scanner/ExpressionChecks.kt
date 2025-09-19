package scanner

import node.BinaryExpression
import node.Expression
import node.IdentifierExpression
import node.ReadInputExpression

object ExpressionChecks {
    fun containsIdentifier(expr: Expression): Boolean =
        when (expr) {
            is IdentifierExpression -> true
            is BinaryExpression ->
                containsIdentifier(expr.getLeft()) || containsIdentifier(expr.getRight())
            else -> false
        }

    fun containsReadInput(expr: Expression): Boolean =
        when (expr) {
            is ReadInputExpression -> true
            is BinaryExpression ->
                containsReadInput(expr.getLeft()) || containsReadInput(expr.getRight())
            else -> false
        }
}
