package factory

import Analyzer
import DefaultAnalyzer
import config.AnalyzerConfig
import rules.CamelCaseChecker
import rules.IdentifierStyle
import rules.SnakeCaseChecker
import rules.AnalyzerRuleRegistry
import rules.NoStyleChecker
import rules.Rule
import type.Version
import type.Version.VERSION_1_1
import type.Version.VERSION_1_0

object AnalyzerFactory : AnalyzerFactoryInterface {

    override fun createAnalyzer(
        version: Version,
        config: AnalyzerConfig,
    ): Analyzer {
        val styleChecker =
            when (config.identifierStyle) {
                IdentifierStyle.CAMEL_CASE -> CamelCaseChecker()
                IdentifierStyle.SNAKE_CASE -> SnakeCaseChecker()
                IdentifierStyle.NO_STYLE -> NoStyleChecker()
            }

        return when (version) {
            VERSION_1_1 ->
                DefaultAnalyzer(
                    AnalyzerRuleRegistry.rulesV11(styleChecker, config.restrictPrintlnArgs),
                )
            VERSION_1_0 ->
                DefaultAnalyzer(
                    AnalyzerRuleRegistry.rulesV10(styleChecker, config.restrictPrintlnArgs),
                    // TODO RARO
                )
        }
    }

    override fun createCustom(rules: List<Rule>): Analyzer = DefaultAnalyzer(rules)
}
