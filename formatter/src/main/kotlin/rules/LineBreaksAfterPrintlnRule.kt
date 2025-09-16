import formatter.config.FormatConfig
import formatter.rules.FormatRule
import formatter.rules.FormatState
import formatter.rules.RuleResult
import type.CommonTypes

class LineBreaksAfterPrintlnRule : FormatRule {

    override fun canHandle(stream: TokenStream, config: FormatConfig): Boolean {
        val currentToken = stream.peak() ?: return false
        // Verificar si es un token de println y si hay configuración definida
        return currentToken.getType() == CommonTypes.PRINT &&
                currentToken.getValue().contains("println")
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState
    ): RuleResult {
        // Consumir el token println
        val printlnToken = stream.next()?.token!!

        // Obtener número de líneas en blanco (0, 1, o 2)
        val blanks = config.blankLinesAfterPrintln!!.coerceIn(0, 2)

        // Construir el texto: println + saltos de línea
        // Si blanks = 0, solo el println
        // Si blanks = 1, println + \n (1 línea en blanco)
        // Si blanks = 2, println + \n\n (2 líneas en blanco)
        val lineBreaks = if (blanks > 0) "\n".repeat(blanks) else ""
        val newText = printlnToken.getValue() + lineBreaks

        // Actualizar estado si agregamos saltos de línea
        val newState = if (blanks > 0) {
            state.copy(isNewLine = true)
        } else {
            state
        }

        return RuleResult(newText = newText, state = newState)
    }
}