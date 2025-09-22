package rules

import config.AnalyzerConfig
import diagnostic.Diagnostic
import node.Program

class RuleRegistry(
    private val rules: List<Rule>,
    private val config: AnalyzerConfig,
) : RuleEnforcer {
    override fun apply(program: Program): List<Diagnostic> {
        val diagnostics: MutableList<Diagnostic> = mutableListOf()
        for (rule in rules) {
            if (rule.canHandle(config)) {
                diagnostics += rule.apply(program)
            }
        }
        return diagnostics
    }
}
