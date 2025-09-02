package analyzer

import coordinates.Position
import node.AssignmentStatement
import node.BinaryExpression
import node.DeclarationStatement
import node.Expression
import node.ExpressionStatement
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program
import node.Statement

class Analyzer(
    private val config: AnalyzerConfig = AnalyzerConfig(),
) {
    fun analyze(program: Program): List<Diagnostic> = analyzeProgram(program)

    private fun analyzeProgram(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEachIndexed { idx, stmt ->
            analyzeStatement(stmt, idx, diags)
        }
        return diags
    }

    private fun analyzeStatement(
        stmt: Statement,
        idx: Int,
        diags: MutableList<Diagnostic>,
    ) {
        when (stmt) {
            is DeclarationStatement -> checkName(stmt.getIdentifier(), idx, diags)
            is AssignmentStatement -> checkName(stmt.getIdentifier(), idx, diags)
            is PrintStatement -> {
                if (config.restrictPrintlnArgs) {
                    val expr = stmt.getExpression()
                    if (expr !is IdentifierExpression && expr !is LiteralExpression) {
                        diags.add(
                            Diagnostic(
                                "println must take only a literal or identifier",
                                Position(idx, 0),
                            ),
                        )
                    }
                }
            }
            is ExpressionStatement -> analyzeExpression(stmt.getExpression(), idx, diags)
            else -> Unit
        }
    }

    private fun analyzeExpression(
        expr: Expression,
        idx: Int,
        diags: MutableList<Diagnostic>,
    ) {
        when (expr) {
            is IdentifierExpression -> checkName(expr.getName(), idx, diags)
            is BinaryExpression -> {
                analyzeExpression(expr.getLeft(), idx, diags)
                analyzeExpression(expr.getRight(), idx, diags)
            }
            else -> Unit
        }
    }

    private fun checkName(
        name: String,
        idx: Int,
        diags: MutableList<Diagnostic>,
    ) {
        val pattern =
            when (config.identifierStyle) {
                IdentifierStyle.CAMEL_CASE -> Regex("^[a-z][A-Za-z0-9]*\$")
                IdentifierStyle.SNAKE_CASE -> Regex("^[a-z][a-z0-9_]*\$")
            }
        if (!pattern.matches(name)) {
            diags.add(
                Diagnostic(
                    "Identifier '$name' does not match ${config.identifierStyle}",
                    Position(idx, 0),
                ),
            )
        }
    }
}
