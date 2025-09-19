package checkers

interface NameChecker {
    val pattern: Regex

    fun styleName(): String
}
