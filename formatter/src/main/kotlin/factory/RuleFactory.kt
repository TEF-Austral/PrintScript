package formatter.factory

import formatter.rules.FormattingRule
import formatter.rules.IfRule
import formatter.rules.PrintlnRule
import formatter.rules.StringLiteralRule
import formatter.rules.token.DefaultTokenFormatter
import formatter.rules.token.TokenFormatter

class RuleFactory(
    private val tokenFormatter: TokenFormatter = DefaultTokenFormatter(),
) {
    fun createIfRule(): IfRule = IfRule(tokenFormatter)

    fun createPrintlnRule(): PrintlnRule = PrintlnRule(tokenFormatter)

    fun createStringLiteralRule(): StringLiteralRule = StringLiteralRule(tokenFormatter)

    fun createAllRules(): List<FormattingRule> =
        listOf(
            createIfRule(),
            createPrintlnRule(),
            createStringLiteralRule(),
        )
}
