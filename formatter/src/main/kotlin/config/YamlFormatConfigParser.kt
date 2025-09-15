package formatter.config

class YamlFormatConfigParser : FormatConfigParser {
    override fun parse(text: String): FormatConfig {
        val entries =
            text
                .lines()
                .map(String::trim)
                .filter { it.isNotEmpty() && !it.startsWith("#") }
                .mapNotNull { line ->
                    line
                        .split(":", limit = 2)
                        .takeIf { it.size == 2 }
                        ?.let { it[0].trim() to it[1].trim() }
                }.toMap()

        return FormatConfig(
            spaceBeforeColon = entries["spaceBeforeColon"]?.toBoolean() ?: false,
            spaceAfterColon = entries["spaceAfterColon"]?.toBoolean() ?: false,
            spaceAroundAssignment = entries["spaceAroundAssignment"]?.toBoolean() ?: true,
            spaceAroundOperators = entries["spaceAroundOperators"]?.toBoolean() ?: true,
            enforceSingleSpace = entries["enforceSingleSpace"]?.toBoolean() ?: true,
            breakAfterStatement = entries["breakAfterStatement"]?.toBoolean() ?: true,
            blankLinesAfterPrintln = entries["blankLinesAfterPrintln"]?.toIntOrNull() ?: 0,
            indentSize =
                entries["indentSize"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE,
            ifBraceOnSameLine = entries["ifBraceOnSameLine"]?.toBoolean() ?: true,
            ifIndentInside =
                entries["ifIndentInside"]?.toIntOrNull()
                    ?: FormatConfig.DEFAULT_IF_INDENT_INSIDE,
        )
    }
}
