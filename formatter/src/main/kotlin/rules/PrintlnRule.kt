// Archivo: formatter/rules/PrintlnRule.kt
package formatter.rules

import BaseRule
import Token
import formatter.engine.FormatterContext
import type.CommonTypes

class PrintlnRule : BaseRule() { // Hereda de BaseRule
    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = token.getType() == CommonTypes.PRINT && token.getValue().contains("println")

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        // 1. Usamos el método 'format' de la clase base para obtener el texto con la indentación correcta.
        val (formattedText, intermediateContext) = format(token, context)

        // 2. Devolvemos el texto y actualizamos el contexto con la bandera para el punto y coma.
        val newContext = intermediateContext.copy(isPrintlnStatement = true)
        return Pair(formattedText, newContext)
    }
}
