package rules

import diagnostic.Diagnostic
import coordinates.Coordinates
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
        program.getStatements().forEach { stmt ->
            when (stmt) {
                is DeclarationStatement -> {
                    check(stmt.getIdentifier(), stmt.getIdentifierToken().getCoordinates(), diags)
                    stmt.getInitialValue()?.visit(diags)
                }
                is AssignmentStatement -> {
                    check(stmt.getIdentifier(), stmt.getIdentifierToken().getCoordinates(), diags)
                    stmt.getValue().visit(diags)
                }
                is PrintStatement -> stmt.getExpression().visit(diags)
                is ExpressionStatement -> stmt.getExpression().visit(diags)
                else -> {}
            }
        }
        return diags
    }

    private fun check(
        name: String,
        coordinates: Coordinates,
        diags: MutableList<Diagnostic>,
    ) {
        if (!checker.pattern.matches(name)) {
            diags +=
                Diagnostic(
                    "Identifier '$name' does not match ${checker.styleName()}",
                    coordinates,
                )
        }
    }

    private fun Expression.visit(diags: MutableList<Diagnostic>) {
        when (this) {
            is IdentifierExpression -> check(getValue(), getToken().getCoordinates(), diags)
            is BinaryExpression -> {
                getLeft().visit(diags)
                getRight().visit(diags)
            }
            else -> {}
        }
    }
}
