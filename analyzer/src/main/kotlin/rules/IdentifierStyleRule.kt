package analyzer.rules

import analyzer.Diagnostic
import coordinates.Position
import node.AssignmentStatement
import node.BinaryExpression
import node.DeclarationStatement
import node.Expression
import node.ExpressionStatement
import node.IdentifierExpression
import node.PrintStatement
import node.Program

class IdentifierStyleRule(
    private val checker: NameChecker,
) : Rule {
    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEachIndexed { idx, stmt ->
            when (stmt) {
                is DeclarationStatement -> {
                    check(stmt.getIdentifier(), idx, diags)
                    stmt.getInitialValue()?.visit(idx, diags)
                }
                is AssignmentStatement -> {
                    check(stmt.getIdentifier(), idx, diags)
                    stmt.getValue().visit(idx, diags)
                }
                is PrintStatement -> stmt.getExpression().visit(idx, diags)
                is ExpressionStatement -> stmt.getExpression().visit(idx, diags)
                else -> {}
            }
        }
        return diags
    }

    private fun check(
        name: String,
        idx: Int,
        diags: MutableList<Diagnostic>,
    ) {
        if (!checker.pattern.matches(name)) {
            diags +=
                Diagnostic(
                    "Identifier '$name' does not match ${checker.styleName()}",
                    Position(idx, 0),
                )
        }
    }

    private fun Expression.visit(
        idx: Int,
        diags: MutableList<Diagnostic>,
    ) {
        when (this) {
            is IdentifierExpression -> check(getValue(), idx, diags)
            is BinaryExpression -> {
                getLeft().visit(idx, diags)
                getRight().visit(idx, diags)
            }
            else -> {}
        }
    }
}
