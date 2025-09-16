import formatter.config.FormatConfig
import formatter.rules.FormatRule
import formatter.rules.FormatState
import formatter.rules.RuleResult
import type.CommonTypes

class LineBreaksAfterPrintlnRule : FormatRule {

    override fun canHandle(
        stream: TokenStream,
        config: FormatConfig,
    ): Boolean {
        val currentToken = stream.peak() ?: return false
        // IMPORTANTE: Verificar que la configuración existe antes de activar la regla
        return currentToken.getType() == CommonTypes.PRINT &&
            currentToken.getValue().contains("println") &&
            config.blankLinesAfterPrintln != null // Verificar que existe la config
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState,
    ): RuleResult {
        // Consumir el token println - SIEMPRE consumir para evitar bucle infinito
        val printlnToken = stream.next()?.token ?: return RuleResult(null, state)

        // Obtener número de líneas en blanco (0, 1, o 2)
        val blanks = config.blankLinesAfterPrintln?.coerceIn(0, 2) ?: 0

        // Construir el texto con los saltos de línea apropiados
        val lineBreaks = if (blanks > 0) "\n".repeat(blanks) else ""
        val newText = printlnToken.getValue() + lineBreaks

        // Actualizar estado
        val newState =
            if (blanks > 0) {
                state.copy(isNewLine = true)
            } else {
                state
            }

        return RuleResult(newText = newText, state = newState)
    }
}
