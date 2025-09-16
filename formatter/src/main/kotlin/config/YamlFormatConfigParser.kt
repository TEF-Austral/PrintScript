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
            spaceBeforeColon = entries["spaceBeforeColon"]?.toBoolean(),
            spaceAfterColon = entries["spaceAfterColon"]?.toBoolean(),
            spaceAroundAssignment = entries["spaceAroundAssignment"]?.toBoolean(),
            spaceAroundOperators = entries["spaceAroundOperators"]?.toBoolean(),
            enforceSingleSpace = entries["enforceSingleSpace"]?.toBoolean(),
            breakAfterStatement = entries["breakAfterStatement"]?.toBoolean(),
            blankLinesAfterPrintln = entries["blankLinesAfterPrintln"]?.toIntOrNull() ?: 0,
            indentSize =
                entries["indentSize"]?.toIntOrNull() ?: FormatConfig.DEFAULT_SIZE,
            ifBraceOnSameLine = entries["ifBraceOnSameLine"]?.toBoolean(),
        )
    }
}
