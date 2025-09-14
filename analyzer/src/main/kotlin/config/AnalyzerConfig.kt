package config

import rules.IdentifierStyle

data class AnalyzerConfig(
    val identifierStyle: IdentifierStyle = IdentifierStyle.NO_STYLE,
    val restrictPrintlnArgs: Boolean = true,
    val restrictReadInputArgs: Boolean = false,
)
