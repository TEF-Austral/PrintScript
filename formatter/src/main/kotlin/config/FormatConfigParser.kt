package formatter.config

interface FormatConfigParser {
    fun parse(text: String): FormatConfig
}
