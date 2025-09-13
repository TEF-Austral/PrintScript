package rules

class NoStyleChecker(
    override val pattern: Regex = Regex(".*"),
) : NameChecker {
    override fun styleName(): String = "No Style"
}