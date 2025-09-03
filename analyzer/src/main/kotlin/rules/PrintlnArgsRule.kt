package analyzer.rules

import analyzer.Diagnostic
import coordinates.Position
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program

class PrintlnArgsRule : Rule {
    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEachIndexed { idx, stmt ->
            if (stmt is PrintStatement) {
                val expr = stmt.getExpression()
                if (expr !is IdentifierExpression && expr !is LiteralExpression) {
                    diags +=
                        Diagnostic(
                            "println must take only a literal or identifier",
                            Position(idx, 0),
                        )
                }
            }
        }
        return diags
    }
}
