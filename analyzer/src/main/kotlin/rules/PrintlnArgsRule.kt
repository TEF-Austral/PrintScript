package rules

import coordinates.Position
import diagnostic.Diagnostic
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program

class PrintlnArgsRule : Rule {
    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEach { stmt ->
            if (stmt is PrintStatement) {
                val expr = stmt.getExpression()
                if (expr !is IdentifierExpression && expr !is LiteralExpression) {
                    val coordinates = stmt.getExpressionToken()?.getCoordinates() ?: Position(0, 0)
                    // Default to (0, 0) if no token is available
                    diags +=
                        Diagnostic(
                            "println must take only a literal or identifier",
                            coordinates,
                        )
                }
            }
        }
        return diags
    }
}
