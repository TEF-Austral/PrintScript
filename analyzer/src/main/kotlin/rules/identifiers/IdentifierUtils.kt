package rules.identifiers

import checkers.PatternChecker
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
import node.Statement

fun checkProgram(
    program: Program,
    checker: PatternChecker,
): List<Diagnostic> {
    val diagnostics = mutableListOf<Diagnostic>()
    program.getStatements().forEach { stmt ->
        checkStatement(stmt, checker, diagnostics)
    }
    return diagnostics
}

private fun checkStatement(
    stmt: Statement,
    checker: PatternChecker,
    diagnostics: MutableList<Diagnostic>,
) {
    when (stmt) {
        is DeclarationStatement -> {
            checkIdentifier(
                stmt.getIdentifier(),
                stmt.getIdentifierToken().getCoordinates(),
                checker,
                diagnostics,
            )
            stmt.getInitialValue()?.let { checkExpression(it, checker, diagnostics) }
        }
        is AssignmentStatement -> {
            checkIdentifier(
                stmt.getIdentifier(),
                stmt.getIdentifierToken().getCoordinates(),
                checker,
                diagnostics,
            )
            checkExpression(stmt.getValue(), checker, diagnostics)
        }
        is PrintStatement -> checkExpression(stmt.getExpression(), checker, diagnostics)
        is ExpressionStatement -> checkExpression(stmt.getExpression(), checker, diagnostics)
        else -> {}
    }
}

private fun checkExpression(
    expression: Expression,
    checker: PatternChecker,
    diagnostics: MutableList<Diagnostic>,
) {
    when (expression) {
        is IdentifierExpression ->
            checkIdentifier(
                expression.getValue(),
                expression.getToken().getCoordinates(),
                checker,
                diagnostics,
            )
        is BinaryExpression -> {
            checkExpression(expression.getLeft(), checker, diagnostics)
            checkExpression(expression.getRight(), checker, diagnostics)
        }
        else -> {}
    }
}

private fun checkIdentifier(
    name: String,
    coordinates: Coordinates,
    checker: PatternChecker,
    diagnostics: MutableList<Diagnostic>,
) {
    if (!checker.pattern.matches(name)) {
        diagnostics +=
            Diagnostic(
                "Identifier '$name' does not match ${checker.styleName()}",
                coordinates,
            )
    }
}
