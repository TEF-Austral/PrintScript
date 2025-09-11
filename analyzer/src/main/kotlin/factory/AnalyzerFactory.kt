package factory

import Analyzer
import DefaultAnalyzer
import config.AnalyzerConfig
import config.AnalyzerConfigLoader
import rules.CamelCaseChecker
import rules.IdentifierStyle
import rules.SnakeCaseChecker
import rules.AnalyzerRuleRegistry
import rules.Rule
import type.Version
import type.Version.VERSION_1_1
import type.Version.VERSION_1_0

object AnalyzerFactory : AnalyzerFactoryInterface {

    override fun createWithVersion(
        version: Version,
        configPath: String?,
    ): Analyzer {
        val baseConfig = configPath?.let(AnalyzerConfigLoader::load) ?: AnalyzerConfig()
        val styleChecker =
            when (baseConfig.identifierStyle) {
                IdentifierStyle.CAMEL_CASE -> CamelCaseChecker()
                IdentifierStyle.SNAKE_CASE -> SnakeCaseChecker()
            }

        return when (version) {
            VERSION_1_1 ->
                DefaultAnalyzer(
                    AnalyzerRuleRegistry.rulesV11(styleChecker, baseConfig.restrictPrintlnArgs),
                )
            VERSION_1_0 ->
                DefaultAnalyzer(
                    AnalyzerRuleRegistry.rulesV10(styleChecker, baseConfig.restrictPrintlnArgs),
                )
        }
    }

    override fun createCustom(rules: List<Rule>): Analyzer = DefaultAnalyzer(rules)
}
