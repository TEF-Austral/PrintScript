package rules

import diagnostic.Diagnostic
import node.Program
import node.ReadInputStatement

class ReadInputArgsRule : Rule {
    override fun apply(program: Program): List<Diagnostic> {
        val diags = mutableListOf<Diagnostic>()
        program.getStatements().forEach { stmt ->
            if (stmt is ReadInputStatement) {
                val arg = stmt.printValue()
                if (!isIdentifier(arg) && !isLiteral(arg)) {
                    diags +=
                        Diagnostic(
                            "readInput must take only a literal or identifier",
                            stmt.getCoordinates(),
                        )
                }
            }
        }
        return diags
    }

    private fun isIdentifier(s: String): Boolean = Regex("^[a-zA-Z_][A-Za-z0-9_]*\$").matches(s)

    private fun isLiteral(s: String): Boolean = isStringLiteral(s) || isBooleanLiteral(s) || isNumberLiteral(s)

    private fun isStringLiteral(s: String): Boolean = s.startsWith("\"") && s.endsWith("\"")

    private fun isBooleanLiteral(s: String): Boolean = s == "true" || s == "false"

    private fun isNumberLiteral(s: String): Boolean = s.toDoubleOrNull() != null
}
