package checkers

import checkers.NameChecker

class CamelCaseChecker : NameChecker {
    override val pattern = Regex("^[a-z][A-Za-z0-9]*\$")

    override fun styleName() = "CAMEL_CASE"
}
