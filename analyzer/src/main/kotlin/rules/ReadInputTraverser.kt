package rules

import diagnostic.Diagnostic
import node.AssignmentStatement
import node.DeclarationStatement
import node.Expression
import node.ExpressionStatement
import node.IfStatement
import node.PrintStatement
import node.Statement
import node.BinaryExpression
import node.ReadInputExpression

class ReadInputTraverser(
    private val validator: ArgValidator,
) {

    fun traverseStatement(stmt: Statement): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        when (stmt) {
            is DeclarationStatement ->
                stmt.getInitialValue()?.let { diagnostics += traverseExpression(it) }
            is AssignmentStatement -> diagnostics += traverseExpression(stmt.getValue())
            is PrintStatement -> diagnostics += traverseExpression(stmt.getExpression())
            is ExpressionStatement -> diagnostics += traverseExpression(stmt.getExpression())
            is IfStatement -> diagnostics += handleIfStatement(stmt)
            else ->
                error(
                    "Unhandled Expression type: ${stmt::class.simpleName} at ${stmt.getCoordinates()}",
                )
        }
        return diagnostics
    }

    private fun handleIfStatement(stmt: IfStatement): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        diagnostics += traverseExpression(stmt.getCondition())
        diagnostics += traverseStatement(stmt.getConsequence())
        stmt.getAlternative()?.let { diagnostics += traverseStatement(it) }
        return diagnostics
    }

    private fun traverseExpression(expr: Expression): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        when (expr) {
            is ReadInputExpression -> {
                val arg = expr.printValue()
                if (!validator.isIdentifier(arg) && !validator.isLiteral(arg)) {
                    diagnostics +=
                        Diagnostic(
                            "readInput must take only a literal or identifier",
                            expr.getCoordinates(),
                        )
                }
            }
            is BinaryExpression -> {
                diagnostics += traverseExpression(expr.getLeft())
                diagnostics += traverseExpression(expr.getRight())
            }
            else -> {}
        }
        return diagnostics
    }
}
