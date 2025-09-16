package formatter.rules

import TokenStream
import formatter.config.FormatConfig
import type.CommonTypes

class LineBreakAfterSemicolonRule : FormatRule {

    override fun canHandle(
        stream: TokenStream,
        config: FormatConfig,
    ): Boolean {
        val currentToken = stream.peak() ?: return false
        // El punto y coma es un DELIMITER
        return currentToken.getType() == CommonTypes.DELIMITERS &&
            currentToken.getValue().contains(";")
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState,
    ): RuleResult {
        // Consumir el token con punto y coma
        val semicolonToken = stream.next()?.token!!

        // Construir el resultado: el token + salto de línea
        val newText = semicolonToken.getValue() + "\n"

        // Actualizar el estado para indicar que estamos en una nueva línea
        val newState = state.copy(isNewLine = true)

        return RuleResult(newText = newText, state = newState)
    }
}
