package rules

import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.IdentifierExpression
import node.LiteralExpression
import node.PrintStatement
import node.Program

class PrintlnArgsRule : Rule {

    override fun canHandle(config: AnalyzerConfig): Boolean = config.restrictPrintlnArgs

    override fun apply(program: Program): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        program.getStatements().forEach { stmt ->
            if (stmt is PrintStatement) {
                val expr = stmt.getExpression()
                if (expr !is IdentifierExpression && expr !is LiteralExpression) {
                    diagnostics +=
                        Diagnostic(
                            "println must take only a literal or identifier",
                            expr.getCoordinates(),
                        )
                }
            }
        }
        return diagnostics
    }
}
