package formatter.rules

import TokenStream
import formatter.config.FormatConfig
import type.CommonTypes

class IfBraceRule : FormatRule {

    override fun canHandle(
        stream: TokenStream,
        config: FormatConfig,
    ): Boolean {
        if (config.ifBraceOnSameLine == null) return false
        return stream.peak()?.getType() == CommonTypes.CONDITIONALS
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState,
    ): RuleResult {
        val sb = StringBuilder()
        var newState = state

        // 1. Consumir 'if' o 'else'.
        var currentToken = stream.next()?.token!!
        sb.append(currentToken.getValue())

        // 2. Consumir la condición si existe.
        if (stream.peak()?.getValue()?.contains("(") == true) {
            sb.append(" ")
            var parenDepth = 0
            do {
                currentToken = stream.next()?.token!!
                val value = currentToken.getValue()
                if (value.contains("(")) parenDepth++
                if (value.contains(")")) parenDepth--
                sb.append(value)
            } while (parenDepth > 0 && !stream.isAtEnd())
        }

        // 3. Formatear la llave y actualizar el estado.
        if (stream.peak()?.getValue()?.contains("{") == true) {
            val braceToken = stream.next()?.token!!

            if (config.ifBraceOnSameLine!!) {
                sb.append(" ").append(braceToken.getValue())
                newState = state.copy(isNewLine = false)
            } else {
                val indentation = " ".repeat(state.indentationLevel)
                sb.append("\n").append(indentation).append(braceToken.getValue())
                newState = state.copy(isNewLine = true)
            }
        }

        // 4. Devolver el resultado con el texto construido y el estado final.
        return RuleResult(newText = sb.toString(), state = newState)
    }
}
