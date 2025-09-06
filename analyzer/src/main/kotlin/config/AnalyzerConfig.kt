package config

import rules.IdentifierStyle

data class AnalyzerConfig(
    val identifierStyle: IdentifierStyle = IdentifierStyle.CAMEL_CASE,
    val restrictPrintlnArgs: Boolean = true,
)
