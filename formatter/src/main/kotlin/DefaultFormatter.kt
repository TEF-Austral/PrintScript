import TokenStream
import formatter.Formatter
import formatter.config.FormatConfig
import formatter.rules.FormatRule
import formatter.rules.FormatState
import formatter.rules.RuleResult
import java.io.Writer

class DefaultFormatter(private val rules: List<FormatRule> = emptyList()) : Formatter {

    override fun formatToString(src: TokenStream, config: FormatConfig): String {
        val output = StringBuilder()
        val state = FormatState()
        format(src, config, state, output)
        return output.toString()
    }

    override fun formatToWriter(src: TokenStream, config: FormatConfig, writer: Writer) {
        val output = StringBuilder()
        val state = FormatState()
        format(src, config, state, output)
        writer.write(output.toString())
        writer.flush()
    }

    private fun format(
        src: TokenStream,
        config: FormatConfig,
        state: FormatState,
        output: StringBuilder
    ): FormatState {
        var currentState = state
        var iterationCount = 0
        val maxIterations = 100000 // Límite de seguridad

        while (!src.isAtEnd()) {
            // Protección contra bucles infinitos
            iterationCount++
            if (iterationCount > maxIterations) {
                throw IllegalStateException("Formatter exceeded maximum iterations. Possible infinite loop detected.")
            }

            val token = src.peak()
            if (token == null) {
                break
            }

            // Buscar una regla que pueda manejar el estado actual del stream
            var ruleApplied = false
            for (rule in rules) {
                if (rule.canHandle(src, config)) {
                    // Aplicar la regla y manejar el resultado
                    val result = rule.apply(src, config, currentState)
                    currentState = handleRuleResult(result, currentState, output, config)
                    ruleApplied = true
                    break // Solo aplicar una regla por token
                }
            }

            // Si ninguna regla se aplicó, consumir el token por defecto
            if (!ruleApplied) {
                val streamResult = src.next()
                if (streamResult == null) {
                    // Si no podemos avanzar, salir del bucle para evitar bucle infinito
                    break
                }

                // Aplicar indentación si estamos al inicio de una nueva línea
                if (currentState.isNewLine && currentState.indentationLevel > 0) {
                    val indent = " ".repeat(currentState.indentationLevel * config.indentSize)
                    output.append(indent)
                    currentState = currentState.copy(isNewLine = false)
                }

                output.append(streamResult.token.getValue())

                // Actualizar estado basado en el contenido del token
                if (streamResult.token.getValue().contains('\n')) {
                    currentState = currentState.copy(isNewLine = true)
                }
            }
        }

        return currentState
    }

    private fun handleRuleResult(
        result: RuleResult,
        state: FormatState,
        output: StringBuilder,
        config: FormatConfig
    ): FormatState {
        // Si hay nuevo texto, agregarlo al output
        result.newText?.let { text ->
            // Aplicar indentación si es necesario antes del nuevo texto
            if (state.isNewLine && state.indentationLevel > 0 && !text.startsWith("\n")) {
                val indent = " ".repeat(state.indentationLevel * config.indentSize)
                output.append(indent)
            }
            output.append(text)
        }

        // Retornar el nuevo estado
        return result.state
    }
}