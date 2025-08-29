package formatter.config

class JsonFormatConfigParser : FormatConfigParser {
    override fun parse(text: String): FormatConfig {
        val entries = text
            .trim()
            .removePrefix("{")
            .removeSuffix("}")
            .split(',')
            .map { it.trim().split(':', limit = 2).map(String::trim) }
            .filter { it.size == 2 }
            .associate { it[0].removeSurrounding("\"") to it[1].removeSurrounding("\"") }

        return FormatConfig(
            spaceBeforeColon = entries["spaceBeforeColon"]?.toBoolean() ?: false,
            spaceAfterColon = entries["spaceAfterColon"]?.toBoolean() ?: true,
            spaceAroundAssignment = entries["spaceAroundAssignment"]?.toBoolean() ?: true,
            blankLinesBeforePrintln = entries["blankLinesBeforePrintln"]?.toIntOrNull() ?: 0,
            indentSize = entries["indentSize"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE
        )
    }
}