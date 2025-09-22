package factory

import Analyzer
import config.AnalyzerConfig
import checkers.IdentifierStyle
import rules.RuleEnforcer
import type.Version

interface AnalyzerFactoryInterface {

    fun createAnalyzer(
        version: Version,
        config: AnalyzerConfig = AnalyzerConfig(IdentifierStyle.SNAKE_CASE, true),
    ): Analyzer

    fun createCustom(rules: RuleEnforcer): Analyzer
}
