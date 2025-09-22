package factory

import Analyzer
import DefaultAnalyzer
import config.AnalyzerConfig
import rules.IdentifierStyleRule
import rules.PrintlnArgsRule
import rules.ReadInputArgsRule
import rules.RuleEnforcer
import rules.RuleRegistry
import type.Version
import type.Version.VERSION_1_1
import type.Version.VERSION_1_0

object AnalyzerFactory : AnalyzerFactoryInterface {

    override fun createAnalyzer(
        version: Version,
        config: AnalyzerConfig,
    ): Analyzer =
        when (version) {
            VERSION_1_0 ->
                DefaultAnalyzer(
                    RuleRegistry(
                        listOf(IdentifierStyleRule(config), PrintlnArgsRule()),
                        config,
                    ),
                )

            VERSION_1_1 ->
                DefaultAnalyzer(
                    RuleRegistry(
                        listOf(
                            IdentifierStyleRule(config),
                            PrintlnArgsRule(),
                            ReadInputArgsRule(),
                        ),
                        config,
                    ),
                )
        }

    override fun createCustom(rules: RuleEnforcer): Analyzer = DefaultAnalyzer(rules)
}
