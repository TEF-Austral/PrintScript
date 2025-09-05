// analyzer/src/main/kotlin/analyzer/JsonAnalyzerConfigParser.kt
package analyzer

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
