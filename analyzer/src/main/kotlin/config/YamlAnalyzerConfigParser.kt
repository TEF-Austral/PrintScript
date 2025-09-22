package config

import checkers.IdentifierStyle
import kotlin.text.get

class YamlAnalyzerConfigParser {
    fun parse(text: String): AnalyzerConfig {
        val entries =
            text
                .lines()
                .map(String::trim)
                .filter { it.isNotEmpty() && !it.startsWith("#") }
                .mapNotNull {
                    val parts = it.split(":", limit = 2)
                    if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
                }.toMap()

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
