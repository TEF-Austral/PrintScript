package factory

import Analyzer
import DefaultAnalyzer
import config.AnalyzerConfig
import config.AnalyzerConfigLoader
import rules.CamelCaseChecker
import rules.IdentifierStyle
import rules.SnakeCaseChecker
import rules.AnalyzerRuleRegistry

object AnalyzerFactory {
    fun create(
        version: String,
        configPath: String? = null,
    ): Analyzer {
        val baseConfig = configPath?.let(AnalyzerConfigLoader::load) ?: AnalyzerConfig()
        val styleChecker =
            when (baseConfig.identifierStyle) {
                IdentifierStyle.CAMEL_CASE -> CamelCaseChecker()
                IdentifierStyle.SNAKE_CASE -> SnakeCaseChecker()
            }

        return when {
            version.startsWith("1.1") ->
                DefaultAnalyzer(
                    AnalyzerRuleRegistry.rulesV11(styleChecker, baseConfig.restrictPrintlnArgs),
                )
            else ->
                DefaultAnalyzer(
                    AnalyzerRuleRegistry.rulesV10(styleChecker, baseConfig.restrictPrintlnArgs),
                )
        }
    }
}
