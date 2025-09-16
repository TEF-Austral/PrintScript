package formatter.rules

import TokenStream
import formatter.config.FormatConfig

class ColonSpacingRule : FormatRule {

    override fun canHandle(stream: TokenStream, config: FormatConfig): Boolean {
        return stream.peak()?.getValue()?.contains(":") ?: false
    }

    override fun apply(stream: TokenStream, config: FormatConfig, state: FormatState): RuleResult {
        // 1. Consumir el token.
        val colonToken = stream.next()?.token!!

        // 2. Reconstruir el texto del token con el espaciado correcto.
        val formattedColon = SpaceUtil.rebuild(
            raw = colonToken.getValue(),
            symbol = ":",
            spaceBefore = config.spaceBeforeColon,
            spaceAfter = config.spaceAfterColon
        )

        // 3. Devolver el resultado con el nuevo texto. El estado no se modifica.
        return RuleResult(newText = formattedColon, state = state)
    }
}