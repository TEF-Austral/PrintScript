package formatter.rules

import TokenStream
import formatter.config.FormatConfig
import type.CommonTypes

class ColonSpacingRule : FormatRule {

    override fun canHandle(stream: TokenStream, config: FormatConfig): Boolean {
        val currentToken = stream.peak() ?: return false
        // Verificar si es un delimitador que contiene ":"
        return currentToken.getType() == CommonTypes.DELIMITERS &&
                currentToken.getValue().contains(":") &&
                (config.spaceBeforeColon != null || config.spaceAfterColon != null)
    }

    override fun apply(stream: TokenStream, config: FormatConfig, state: FormatState): RuleResult {
        val colonToken = stream.next()?.token!!

        val formattedColon = SpaceUtil.rebuild(
            raw = colonToken.getValue(),
            symbol = ":",
            spaceBefore = config.spaceBeforeColon,  // null = preservar original
            spaceAfter = config.spaceAfterColon     // null = preservar original
        )

        return RuleResult(newText = formattedColon, state = state)
    }
}