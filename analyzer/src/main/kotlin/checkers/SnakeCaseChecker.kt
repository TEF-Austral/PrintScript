package checkers

class SnakeCaseChecker : PatternChecker {
    override val pattern = Regex("^[a-z][a-z0-9_]*\$")

    override fun styleName() = "SNAKE_CASE"

    override fun getType(): IdentifierStyle = IdentifierStyle.SNAKE_CASE
}
