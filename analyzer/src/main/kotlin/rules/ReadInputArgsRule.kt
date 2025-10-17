package rules

import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program
import node.Statement
import node.Expression
import node.DeclarationStatement
import node.AssignmentStatement
import node.PrintStatement
import node.ExpressionStatement
import node.IfStatement
import node.ReadInputExpression
import node.BinaryExpression
import node.IdentifierExpression
import node.LiteralExpression

class ReadInputArgsRule : Rule {
    override fun canHandle(config: AnalyzerConfig): Boolean = config.restrictReadInputArgs

    override fun apply(program: Program): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        program.getStatements().forEach { stmt ->
            diagnostics += traverseStatement(stmt)
        }
        return diagnostics
    }

    private fun traverseStatement(stmt: Statement): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        when (stmt) {
            is DeclarationStatement ->
                stmt.getInitialValue()?.let {
                    diagnostics +=
                        traverseExpression(it)
                }
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
        if (expr is ReadInputExpression) {
            val arg = expr.printValue()
            if (arg !is IdentifierExpression && arg !is LiteralExpression) {
                diagnostics +=
                    Diagnostic(
                        "readInput must take only a literal or identifier",
                        expr.getCoordinates(),
                    )
            }
        } else if (expr is BinaryExpression) {
            diagnostics += traverseExpression(expr.getLeft())
            diagnostics += traverseExpression(expr.getRight())
        }
        return diagnostics
    }
}
