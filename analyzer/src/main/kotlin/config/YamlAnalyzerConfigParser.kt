package config

import rules.IdentifierStyle

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

        return AnalyzerConfig(
            identifierStyle =
                entries["identifierStyle"]?.let(IdentifierStyle::valueOf)
                    ?: AnalyzerConfig().identifierStyle,
            restrictPrintlnArgs =
                entries["restrictPrintlnArgs"]?.toBoolean()
                    ?: AnalyzerConfig().restrictPrintlnArgs,
        )
    }
}
