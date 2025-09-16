import formatter.config.FormatConfig
import formatter.rules.FormatRule
import formatter.rules.FormatState
import formatter.rules.RuleResult
import formatter.rules.SpaceUtil
import type.CommonTypes

class SpaceAroundOperatorsRule : FormatRule {

    override fun canHandle(
        stream: TokenStream,
        config: FormatConfig,
    ): Boolean {
        val nextToken = stream.peak() ?: return false
        return nextToken.getType() == CommonTypes.OPERATORS &&
            config.spaceAroundOperators != null
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState,
    ): RuleResult {
        // SIEMPRE consumir el token para evitar bucle infinito
        val tokenResult = stream.next() ?: return RuleResult(null, state)
        val currentToken = tokenResult.token

        val spaceAround = config.spaceAroundOperators ?: false
        val operatorSymbol = currentToken.getValue().trim()

        val formattedOperator =
            SpaceUtil.rebuild(
                raw = currentToken.getValue(),
                symbol = operatorSymbol,
                spaceBefore = spaceAround,
                spaceAfter = spaceAround,
            )

        return RuleResult(newText = formattedOperator, state = state)
    }
}
