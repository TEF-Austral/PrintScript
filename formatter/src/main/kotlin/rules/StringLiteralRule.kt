package formatter.rules

import BaseRule
import PrintScriptToken
import Token
import formatter.engine.FormatterContext
import type.CommonTypes

class StringLiteralRule : BaseRule() {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getType() == CommonTypes.STRING_LITERAL

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        // Reconstruye el valor del token con las comillas que el lexer pudo haber quitado.
        val valueWithQuotes = "\"${token.getValue()}\""

        // Crea un token temporal para pasarlo al formateador base,
        // ya que no podemos modificar el token original.
        val tempToken = PrintScriptToken(token.getType(), valueWithQuotes, token.getCoordinates())

        // Llama al método 'format' de BaseRule para manejar la indentación y el espaciado.
        return format(tempToken, context)
    }
}

// Nota: Asegúrate de que tu interfaz 'Token' sea un 'data class' o tenga un método 'copy'
// para que la línea 'token.copy(value = valueWithQuotes)' funcione.
