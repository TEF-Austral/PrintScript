package rules

import diagnostic.Diagnostic
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program

class PrintlnArgsRule : Rule {
    override fun canHandle(summary: ProgramSummary): Boolean = summary.hasPrintStatement

    override fun canHandle(program: Program): Boolean =
        program.getStatements().any { it is PrintStatement }

    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEach { stmt ->
            if (stmt is PrintStatement) {
                val expr = stmt.getExpression()
                if (expr !is IdentifierExpression && expr !is LiteralExpression) {
                    diags +=
                        Diagnostic(
                            "println must take only a literal or identifier",
                            expr.getCoordinates(),
                        )
                }
            }
        }
        return diags
    }
}
