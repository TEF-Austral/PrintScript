package factory

import Analyzer
import DefaultAnalyzer
import config.AnalyzerConfig
import config.AnalyzerConfigLoader
import rules.CamelCaseChecker
import rules.IdentifierStyle
import rules.SnakeCaseChecker
import rules.AnalyzerRuleRegistry
import type.Version
import type.Version.VERSION_1_1
import type.Version.VERSION_1_0

object AnalyzerFactory {
    fun create(
        version: Version,
        configPath: String? = null,
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
}
