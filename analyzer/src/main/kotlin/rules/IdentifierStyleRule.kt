package rules

import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program
import rules.identifiers.CamelCaseRule
import rules.identifiers.NoStyleRule
import rules.identifiers.SnakeCaseRule

class IdentifierStyleRule(
    private val config: AnalyzerConfig,
) : Rule {
    private val styleRules =
        listOf(
            CamelCaseRule(),
            SnakeCaseRule(),
            NoStyleRule(),
        )

    override fun canHandle(config: AnalyzerConfig): Boolean =
        styleRules.any { it.canHandle(config) }

    override fun apply(program: Program): List<Diagnostic> {
        val diagnostics = mutableListOf<Diagnostic>()
        styleRules.forEach { rule ->
            if (rule.canHandle(config)) {
                diagnostics.addAll(rule.apply(program))
            }
        }
        return diagnostics
    }
}
