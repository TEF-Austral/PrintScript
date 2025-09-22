package checkers

class NoStyleChecker(
    override val pattern: Regex = Regex(".*"),
) : PatternChecker {
    override fun styleName(): String = "No Style"

    override fun getType(): IdentifierStyle = IdentifierStyle.NO_STYLE
}
