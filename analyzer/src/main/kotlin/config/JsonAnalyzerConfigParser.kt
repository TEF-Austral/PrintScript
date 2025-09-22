package config

import checkers.IdentifierStyle

class JsonAnalyzerConfigParser {
    fun parse(text: String): AnalyzerConfig {
        val entries =
            text
                .trim()
                .removePrefix("{")
                .removeSuffix("}")
                .split(',')
                .map { it.trim().split(':', limit = 2).map(String::trim) }
                .filter { it.size == 2 }
                .associate { it[0].removeSurrounding("\"") to it[1].removeSurrounding("\"") }

        val base = AnalyzerConfig()
        return AnalyzerConfig(
            identifierStyle =
                entries["identifierStyle"]?.let(IdentifierStyle::valueOf) ?: base.identifierStyle,
            restrictPrintlnArgs =
                entries["restrictPrintlnArgs"]?.toBoolean() ?: base.restrictPrintlnArgs,
            restrictReadInputArgs =
                entries["restrictReadInputArgs"]?.toBoolean() ?: base.restrictReadInputArgs,
        )
    }
}
