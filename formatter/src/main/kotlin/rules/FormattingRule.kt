package formatter.rules

import Token
import formatter.engine.FormatterContext

interface FormattingRule {

    fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean

    /**
     * Aplica la lógica de formato.
     * Retorna el texto formateado para este token y el nuevo contexto para el siguiente.
     */
    fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext>
}
