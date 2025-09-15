package formatter.config

class JsonFormatConfigParser : FormatConfigParser {
    override fun parse(text: String): FormatConfig {
        val entries =
            text
                .trim()
                .removePrefix("{")
                .removeSuffix("}")
                .split(',')
                .map { it.trim().split(':', limit = 2).map(String::trim) }
                .filter { it.size == 2 }
                .associate { it[0].removeSurrounding("\"") to it[1].removeSurrounding("\"") }

        return FormatConfig(
            spaceBeforeColon = entries["spaceBeforeColon"]?.toBoolean() ?: false,
            spaceAfterColon = entries["spaceAfterColon"]?.toBoolean() ?: false,
            spaceAroundAssignment = entries["spaceAroundAssignment"]?.toBoolean() ?: false,
            spaceAroundOperators = entries["spaceAroundOperators"]?.toBoolean() ?: false,
            enforceSingleSpace = entries["enforceSingleSpace"]?.toBoolean() ?: false,
            breakAfterStatement = entries["breakAfterStatement"]?.toBoolean() ?: true,
            blankLinesAfterPrintln = entries["blankLinesAfterPrintln"]?.toIntOrNull() ?: 0,
            indentSize =
                entries["indentSize"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE,
            ifBraceOnSameLine = entries["ifBraceOnSameLine"]?.toBoolean() ?: true,
            ifIndentInside =
                entries["ifIndentInside"]?.toIntOrNull() ?: FormatConfig.DEFAULT_IF_INDENT_INSIDE,
        )
    }
}
