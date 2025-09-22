package formatter.factory

import formatter.Formatter
import formatter.DefaultFormatter
import formatter.engine.DefaultLinePostProcessor
import formatter.engine.FormattingEngine
import formatter.engine.FormattingEngineInt
import formatter.rules.FormattingRule
import formatter.rules.PrintlnRule
import formatter.rules.SemicolonRule
import formatter.rules.ColonRule
import AssignmentRule
import formatter.rules.OperatorRule
import DefaultTokenRule
import FormattingRuleRegistry
import formatter.rules.StringLiteralRule
import formatter.rules.token.DefaultTokenFormatter
import formatter.rules.token.TokenFormatter
import formatter.rules.IfRule
import formatter.rules.OpenBraceRule
import formatter.rules.CloseBraceRule
import type.Version

object DefaultFormatterFactory {
    fun createFormatter(version: Version): Formatter =
        when (version) {
            Version.VERSION_1_0 -> createv10formatter()
            Version.VERSION_1_1 -> createv11formatter()
        }

    private fun createv10formatter(): Formatter {
        val tokenFormatter: TokenFormatter = DefaultTokenFormatter()
        val rules: List<FormattingRule> =
            listOf(
                PrintlnRule(tokenFormatter),
                StringLiteralRule(tokenFormatter),
                SemicolonRule(),
                ColonRule(),
                AssignmentRule(),
                OperatorRule(),
                DefaultTokenRule(),
            )
        val engine: FormattingEngineInt =
            FormattingEngine(
                rootRule = FormattingRuleRegistry(rules),
                postProcessors = listOf(DefaultLinePostProcessor()),
            )
        return DefaultFormatter(engine)
    }

    private fun createv11formatter(): Formatter {
        val tokenFormatter: TokenFormatter = DefaultTokenFormatter()
        val rules: List<FormattingRule> =
            listOf(
                PrintlnRule(tokenFormatter),
                StringLiteralRule(tokenFormatter),
                SemicolonRule(),
                ColonRule(),
                AssignmentRule(),
                OperatorRule(),
                IfRule(tokenFormatter),
                OpenBraceRule(),
                CloseBraceRule(),
                DefaultTokenRule(),
            )
        val engine: FormattingEngineInt =
            FormattingEngine(
                rootRule = FormattingRuleRegistry(rules),
                postProcessors = listOf(DefaultLinePostProcessor()),
            )
        return DefaultFormatter(engine)
    }
}
