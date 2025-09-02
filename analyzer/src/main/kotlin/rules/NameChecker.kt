package analyzer.rules

interface NameChecker {
    val pattern: Regex
    fun styleName(): String
}