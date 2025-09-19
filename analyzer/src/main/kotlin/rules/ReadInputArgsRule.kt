package rules

import diagnostic.Diagnostic
import node.Program
import node.Statement
import node.DeclarationStatement
import node.AssignmentStatement
import node.PrintStatement
import node.ExpressionStatement
import node.IfStatement
import node.ReadInputExpression
import scanner.ExpressionChecks
import scanner.forEachExpression

class ReadInputArgsRule : Rule {
    override fun canHandle(summary: ProgramSummary): Boolean = summary.hasReadInput

    override fun canHandle(program: Program): Boolean =
        program.getStatements().any { stmt ->
            when (stmt) {
                is DeclarationStatement ->
                    stmt.getInitialValue()?.let { ExpressionChecks.containsReadInput(it) }
                        ?: false
                is AssignmentStatement -> ExpressionChecks.containsReadInput(stmt.getValue())
                is PrintStatement -> ExpressionChecks.containsReadInput(stmt.getExpression())
                is ExpressionStatement -> ExpressionChecks.containsReadInput(stmt.getExpression())
                is IfStatement ->
                    ExpressionChecks.containsReadInput(stmt.getCondition()) ||
                        stmt.getConsequence().let { containsReadInputInStatement(it) } ||
                        stmt.getAlternative()?.let { containsReadInputInStatement(it) } ?: false
                else -> false
            }
        }

    private fun containsReadInputInStatement(stmt: Statement): Boolean =
        when (stmt) {
            is DeclarationStatement ->
                stmt.getInitialValue()?.let { ExpressionChecks.containsReadInput(it) }
                    ?: false
            is AssignmentStatement -> ExpressionChecks.containsReadInput(stmt.getValue())
            is PrintStatement -> ExpressionChecks.containsReadInput(stmt.getExpression())
            is ExpressionStatement -> ExpressionChecks.containsReadInput(stmt.getExpression())
            is IfStatement ->
                ExpressionChecks.containsReadInput(stmt.getCondition()) ||
                    containsReadInputInStatement(stmt.getConsequence()) ||
                    stmt.getAlternative()?.let { containsReadInputInStatement(it) } ?: false
            else -> false
        }

    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()

        for (stmt in program.getStatements()) {
            stmt.forEachExpression { expr ->
                if (expr is ReadInputExpression) {
                    val arg = expr.printValue()
                    if (!isIdentifier(arg) && !isLiteral(arg)) {
                        diags +=
                            Diagnostic(
                                "readInput must take only a literal or identifier",
                                expr.getCoordinates(),
                            )
                    }
                }
            }
        }

        return diags
    }

    private fun isIdentifier(s: String): Boolean = Regex("^[a-zA-Z_][A-Za-z0-9_]*\$").matches(s)

    private fun isLiteral(s: String): Boolean =
        isStringLiteral(s) || isBooleanLiteral(s) || isNumberLiteral(s)

    private fun isStringLiteral(s: String): Boolean = s.startsWith("\"") && s.endsWith("\"")

    private fun isBooleanLiteral(s: String): Boolean = s == "true" || s == "false"

    private fun isNumberLiteral(s: String): Boolean = s.toDoubleOrNull() != null
}
