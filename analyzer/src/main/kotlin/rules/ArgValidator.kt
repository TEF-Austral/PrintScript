package rules

class ArgValidator {
    private val identifierRegex = Regex("^[a-zA-Z_][A-Za-z0-9_]*$")

    fun isIdentifier(s: String): Boolean = identifierRegex.matches(s)

    fun isStringLiteral(s: String): Boolean = s.startsWith("\"") && s.endsWith("\"")

    fun isBooleanLiteral(s: String): Boolean = s == "true" || s == "false"

    fun isNumberLiteral(s: String): Boolean = s.toDoubleOrNull() != null

    fun isLiteral(s: String): Boolean =
        isStringLiteral(s) || isBooleanLiteral(s) || isNumberLiteral(s)
}
