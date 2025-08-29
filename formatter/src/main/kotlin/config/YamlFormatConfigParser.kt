package formatter.config

class YamlFormatConfigParser : FormatConfigParser {
    override fun parse(text: String): FormatConfig {
        val entries =
            text
                .lines()
                .map(String::trim)
                .filter { it.isNotEmpty() && !it.startsWith("#") }
                .mapNotNull { line ->
                    val parts = line.split(":", limit = 2)
                    if (parts.size == 2) parts[0].trim() to parts[1].trim() else null
                }.toMap()

        return FormatConfig(
            spaceBeforeColon = entries["spaceBeforeColon"]?.toBoolean() ?: false,
            spaceAfterColon = entries["spaceAfterColon"]?.toBoolean() ?: true,
            spaceAroundAssignment = entries["spaceAroundAssignment"]?.toBoolean() ?: true,
            blankLinesBeforePrintln = entries["blankLinesBeforePrintln"]?.toIntOrNull() ?: 0,
            indentSize = entries["indentSize"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE,
        )
    }
}
