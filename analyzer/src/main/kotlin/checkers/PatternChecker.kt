package checkers

interface PatternChecker {
    val pattern: Regex

    fun styleName(): String

    fun getType(): IdentifierStyle
}
