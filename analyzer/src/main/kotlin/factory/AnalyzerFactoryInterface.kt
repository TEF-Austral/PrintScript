package factory

import Analyzer
import rules.Rule
import type.Version

interface AnalyzerFactoryInterface {

    fun createWithVersion(
        version: Version,
        configPath: String? = null,
    ): Analyzer

    fun createCustom(rules: List<Rule>): Analyzer
}
