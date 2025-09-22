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
            if (!isIdentifier(arg) && !isLiteral(arg)) {
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

    private fun isIdentifier(s: String): Boolean = Regex("^[a-zA-Z_][A-Za-z0-9_]*$").matches(s)

    private fun isLiteral(s: String): Boolean =
        isStringLiteral(s) || isBooleanLiteral(s) || isNumberLiteral(s)

    private fun isStringLiteral(s: String): Boolean = s.startsWith("\"") && s.endsWith("\"")

    private fun isBooleanLiteral(s: String): Boolean = s == "true" || s == "false"

    private fun isNumberLiteral(s: String): Boolean = s.toDoubleOrNull() != null
}
