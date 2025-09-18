// Archivo: formatter/rules/CloseBraceRule.kt (Corregido)
package formatter.rules

import Token
import formatter.engine.FormatterContext

class CloseBraceRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getValue().trim() == "}"

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        // 1. Primero, creamos el nuevo contexto con el nivel de indentación reducido.
        val deindentedContext = context.decreaseIndent()

        // 2. Obtenemos la cadena de indentación para ESE NUEVO nivel.
        val indentation =
            deindentedContext.indentationManager.getIndentation(
                deindentedContext.indentLevel,
            )

        // 3. Construimos el texto: la indentación correcta seguida de la llave.
        val formattedText = "$indentation}\n"

        // 4. Devolvemos el texto y el contexto actualizado.
        return Pair(formattedText, deindentedContext.withNewLine())
    }
}
