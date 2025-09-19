package rules

import checkers.NameChecker
import diagnostic.Diagnostic
import coordinates.Coordinates
import node.DeclarationStatement
import node.ExpressionStatement
import node.IdentifierExpression
import node.PrintStatement
import node.Program
import scanner.ExpressionChecks
import node.AssignmentStatement
import scanner.forEachExpression

class IdentifierStyleRule(
    private val checker: NameChecker,
) : Rule {
    override fun canHandle(summary: ProgramSummary): Boolean = summary.hasIdentifier

    override fun canHandle(program: Program): Boolean =
        program.getStatements().any { stmt ->
            when (stmt) {
                is DeclarationStatement -> true
                is AssignmentStatement -> true
                is PrintStatement -> ExpressionChecks.containsIdentifier(stmt.getExpression())
                is ExpressionStatement -> ExpressionChecks.containsIdentifier(stmt.getExpression())
                else -> false
            }
        }

    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()

        fun check(
            name: String,
            coordinates: Coordinates,
        ) {
            if (!checker.pattern.matches(name)) {
                diags +=
                    Diagnostic(
                        "Identifier '$name' does not match ${checker.styleName()}",
                        coordinates,
                    )
            }
        }

        for (stmt in program.getStatements()) {
            when (stmt) {
                is DeclarationStatement -> {
                    check(stmt.getIdentifier(), stmt.getIdentifierToken().getCoordinates())
                    stmt.getInitialValue()?.forEachExpression { expr ->
                        if (expr is IdentifierExpression) {
                            check(
                                expr.getValue(),
                                expr.getToken().getCoordinates(),
                            )
                        }
                    }
                }
                is AssignmentStatement -> {
                    check(stmt.getIdentifier(), stmt.getIdentifierToken().getCoordinates())
                    stmt.getValue().forEachExpression { expr ->
                        if (expr is IdentifierExpression) {
                            check(
                                expr.getValue(),
                                expr.getToken().getCoordinates(),
                            )
                        }
                    }
                }
                is PrintStatement, is ExpressionStatement -> {
                    val expr =
                        when (stmt) {
                            is PrintStatement -> stmt.getExpression()
                            is ExpressionStatement -> stmt.getExpression()
                            else -> null
                        }
                    expr?.forEachExpression { e ->
                        if (e is IdentifierExpression) {
                            check(
                                e.getValue(),
                                e.getToken().getCoordinates(),
                            )
                        }
                    }
                }
                else -> {}
            }
        }
        return diags
    }
}
