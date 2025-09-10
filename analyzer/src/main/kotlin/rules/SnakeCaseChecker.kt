package rules

class SnakeCaseChecker : NameChecker {
    override val pattern = Regex("^[a-z][a-z0-9_]*\$")

    override fun styleName() = "SNAKE_CASE"
}
