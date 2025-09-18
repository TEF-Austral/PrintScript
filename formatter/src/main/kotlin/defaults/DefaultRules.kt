package formatter.defaults

import AssignmentRule
import ColonRule
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

object DefaultRules {
    fun createDefaultRootRule(): FormattingRule {
        // IMPORTANT: Order matters! More specific rules must come before DefaultTokenRule
        val rules =
            listOf(
                // Specific token rules (these should be checked first)
                PrintlnRule(),
                IfRule(),
                StringLiteralRule(),
                OpenBraceRule(),
                CloseBraceRule(),
                SemicolonRule(),
                ColonRule(),
                AssignmentRule(),
                OperatorRule(),
                ParenthesesRule(),
                // Default rule MUST BE LAST - it handles anything not caught above
                DefaultTokenRule(),
            )

        return FormattingRuleRegistry(rules)
    }
}
