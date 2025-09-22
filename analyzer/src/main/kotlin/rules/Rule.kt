package rules

import config.AnalyzerConfig

interface Rule : RuleEnforcer {
    fun canHandle(config: AnalyzerConfig): Boolean
}
