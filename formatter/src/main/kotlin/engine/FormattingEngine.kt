// Archivo: FormattingEngine.kt
package formatter.engine

import TokenStream
import formatter.config.FormatConfig
import formatter.rules.FormattingRule
import type.CommonTypes

class FormattingEngine(
    private val rootRule: FormattingRule,
    private val postProcessors: List<LinePostProcessor>,
) : FormattingEngineInt {

    override fun format(
        src: TokenStream,
        config: FormatConfig,
    ): String {
        val output = StringBuilder()
        var context = FormatterContext(config) // 1. Se inicializa el contexto inmutable.
        var stream = src

        while (!stream.isAtEnd()) {
            val result = stream.next() ?: break
            val token = result.token
            stream = result.nextStream

            if (token.getType() == CommonTypes.EMPTY) continue

            // 2. Se aplica la regla raíz, que internamente encontrará la sub-regla correcta.
            val (formattedText, nextContext) = rootRule.apply(token, context)

            // 3. Se acumula el resultado y se actualiza el estado para la siguiente iteración.
            output.append(formattedText)
            context = nextContext.copy(previousToken = token)
        }

        return applyPostProcessors(output.toString(), config).trimEnd()
    }

    private fun applyPostProcessors(
        text: String,
        config: FormatConfig,
    ): String = postProcessors.fold(text) { acc, pp -> pp.process(acc, config) }
}
