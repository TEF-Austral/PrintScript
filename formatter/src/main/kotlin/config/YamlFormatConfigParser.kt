package formatter.config

import formatter.rules.RuleId

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

        fun parseEnabled(raw: String?): Set<RuleId> =
            raw
                ?.let {
                    Regex("""\w+""")
                        .findAll(it)
                        .map { m ->
                            m.value
                        }.mapNotNull { runCatching { RuleId.valueOf(it) }.getOrNull() }
                        .toSet()
                }
                ?: emptySet()

        return FormatConfig(
            spaceBeforeColon = entries["spaceBeforeColon"]?.toBoolean(),
            spaceAfterColon = entries["spaceAfterColon"]?.toBoolean(),
            spaceAroundAssignment = entries["spaceAroundAssignment"]?.toBoolean(),
            spaceAroundOperators = entries["spaceAroundOperators"]?.toBoolean(),
            enforceSingleSpace = entries["enforceSingleSpace"]?.toBoolean(),
            breakAfterStatement = entries["breakAfterStatement"]?.toBoolean(),
            blankLinesAfterPrintln = entries["blankLinesAfterPrintln"]?.toIntOrNull() ?: 0,
            indentSize =
                entries["indentSize"]?.toIntOrNull() ?: FormatConfig.DEFAULT_INDENT_SIZE,
            ifBraceOnSameLine = entries["ifBraceOnSameLine"]?.toBoolean(),
            ifIndentInside =
                entries["ifIndentInside"]?.toIntOrNull() ?: FormatConfig.DEFAULT_IF_INDENT_INSIDE,
            enabledRules = parseEnabled(entries["enabledRules"]),
        )
    }
}
