package checkers

import checkers.NameChecker

class NoStyleChecker(
    override val pattern: Regex = Regex(".*"),
) : NameChecker {
    override fun styleName(): String = "No Style"
}
