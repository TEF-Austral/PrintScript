// Archivo: formatter/rules/OpenBraceRule.kt (o IndentRule.kt)
package formatter.rules

import Token
import formatter.engine.FormatterContext

class OpenBraceRule : FormattingRule {
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getValue().trim() == "{"

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val formattedText: String

        // Verifica si la llave pertenece a un 'if' Y si la configuración indica que debe ir en la misma línea.
        if (context.expectingIfBrace && context.config.ifBraceOnSameLine == true) {
            // Caso 1: Llave en la misma línea (comportamiento esperado en el test).
            // Añade un espacio antes de la llave.
            formattedText = " {\n"
        } else {
            // Caso 2: Llave en una nueva línea (para otros casos o si ifBraceOnSameLine = false).
            // Calcula la indentación correcta para la nueva línea.
            val indentation = context.indentationManager.getIndentation(context.indentLevel)
            formattedText = "\n" + indentation + "{\n"
        }

        // La lógica para actualizar el contexto sigue siendo la misma.
        val newContext = context.increaseIndent().withNewLine().copy(expectingIfBrace = false)
        return Pair(formattedText, newContext)
    }
}
