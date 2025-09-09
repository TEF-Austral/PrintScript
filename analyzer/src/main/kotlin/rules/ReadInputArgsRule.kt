package rules

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
import node.ReadEnvExpression
import node.EmptyExpression

class ReadInputArgsRule : Rule {
    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEach { stmt ->
            traverseStatement(stmt, diags)
        }
        return diags
    }

    private fun traverseStatement(
        stmt: Statement,
        diags: MutableList<Diagnostic>,
    ) {
        when (stmt) {
            is DeclarationStatement -> stmt.getInitialValue()?.let { traverseExpression(it, diags) }
            is AssignmentStatement -> traverseExpression(stmt.getValue(), diags)
            is PrintStatement -> traverseExpression(stmt.getExpression(), diags)
            is ExpressionStatement -> traverseExpression(stmt.getExpression(), diags)
            is IfStatement -> {
                traverseExpression(stmt.getCondition(), diags)
                traverseStatement(stmt.getConsequence(), diags)
                stmt.getAlternative()?.let { traverseStatement(it, diags) }
            }
            else -> {}
        }
    }

    private fun traverseExpression(
        expr: Expression,
        diags: MutableList<Diagnostic>,
    ) {
        when (expr) {
            is ReadInputExpression -> {
                val arg = expr.printValue()
                if (!isIdentifier(arg) && !isLiteral(arg)) {
                    diags +=
                        Diagnostic(
                            "readInput must take only a literal or identifier",
                            expr.getCoordinates(),
                        )
                }
            }
            is BinaryExpression -> {
                traverseExpression(expr.getLeft(), diags)
                traverseExpression(expr.getRight(), diags)
            }
            // skip leaves
            is IdentifierExpression,
            is LiteralExpression,
            is ReadEnvExpression,
            is EmptyExpression,
            -> {}
            else -> {} // <-- added to make the when exhaustive
        }
    }

    private fun isIdentifier(s: String): Boolean = Regex("^[a-zA-Z_][A-Za-z0-9_]*\$").matches(s)

    private fun isLiteral(s: String): Boolean = isStringLiteral(s) || isBooleanLiteral(s) || isNumberLiteral(s)

    private fun isStringLiteral(s: String): Boolean = s.startsWith("\"") && s.endsWith("\"")

    private fun isBooleanLiteral(s: String): Boolean = s == "true" || s == "false"

    private fun isNumberLiteral(s: String): Boolean = s.toDoubleOrNull() != null
}
