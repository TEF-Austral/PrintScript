package formatter.factory

import AssignmentRule
import formatter.rules.ColonRule
import DefaultTokenRule
import FormattingRuleRegistry
import ParenthesesRule
import formatter.rules.CloseBraceRule
import formatter.rules.FormattingRule
import formatter.rules.IfRule
import formatter.rules.OpenBraceRule
import formatter.rules.OperatorRule
import formatter.rules.PrintlnRule
import formatter.rules.SemicolonRule
import formatter.rules.StringLiteralRule
import formatter.rules.token.DefaultTokenFormatter
import formatter.rules.token.TokenFormatter

object DefaultRules {

    fun createDefaultRegistryRule(): FormattingRule =
        createDefaultRegistryRuleWithTokenFormatter(DefaultTokenFormatter())

    fun createDefaultRegistryRuleWithTokenFormatter(
        tokenFormatter: TokenFormatter,
    ): FormattingRule {
        val rules =
            listOf(
                PrintlnRule(tokenFormatter),
                IfRule(tokenFormatter),
                StringLiteralRule(tokenFormatter),
                OpenBraceRule(),
                CloseBraceRule(),
                SemicolonRule(),
                ColonRule(),
                AssignmentRule(),
                OperatorRule(),
                ParenthesesRule(),
                DefaultTokenRule(),
            )

        return FormattingRuleRegistry(rules)
    }
}
