package factory

import Analyzer
import config.AnalyzerConfig
import rules.IdentifierStyle
import rules.Rule
import type.Version

interface AnalyzerFactoryInterface {

    fun createAnalyzer(
        version: Version,
        config: AnalyzerConfig = AnalyzerConfig(IdentifierStyle.SNAKE_CASE, true),
    ): Analyzer

    fun createCustom(rules: List<Rule>): Analyzer
}
