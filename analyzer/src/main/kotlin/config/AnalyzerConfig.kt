package config

import checkers.IdentifierStyle

data class AnalyzerConfig(
    val identifierStyle: IdentifierStyle = IdentifierStyle.NO_STYLE,
    val restrictPrintlnArgs: Boolean = true,
    val restrictReadInputArgs: Boolean = false,
    val noReadInput: Boolean = false,
)
