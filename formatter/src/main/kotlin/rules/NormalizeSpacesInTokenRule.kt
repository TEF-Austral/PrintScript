package formatter.rules

import TokenStream
import formatter.config.FormatConfig

class NormalizeSpacesInTokenRule : FormatRule {

    override fun canHandle(
        stream: TokenStream,
        config: FormatConfig,
    ): Boolean {
        val currentToken = stream.peak() ?: return false
        val value = currentToken.getValue()

        // Activar si el token contiene múltiples espacios consecutivos o tabs
        return value.contains("  ") || value.contains("\t")
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState,
    ): RuleResult {
        // Consumir el token
        val token = stream.next()?.token!!
        var normalized = token.getValue()

        // Reemplazar tabs por espacios
        normalized = normalized.replace("\t", " ")

        // Reemplazar múltiples espacios por uno solo
        while (normalized.contains("  ")) {
            normalized = normalized.replace("  ", " ")
        }

        return RuleResult(newText = normalized, state = state)
    }
}
