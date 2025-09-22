package checkers

class CamelCaseChecker : PatternChecker {
    override val pattern = Regex("^[a-z][A-Za-z0-9]*\$")

    override fun styleName() = "CAMEL_CASE"

    override fun getType(): IdentifierStyle = IdentifierStyle.CAMEL_CASE
}
