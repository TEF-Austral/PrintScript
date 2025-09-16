import formatter.config.FormatConfig
import formatter.rules.FormatRule
import formatter.rules.FormatState
import formatter.rules.RuleResult
import formatter.rules.SpaceUtil
import type.CommonTypes

class SpaceAroundAssignmentRule : FormatRule {

    override fun canHandle(stream: TokenStream, config: FormatConfig): Boolean {
        val nextToken = stream.peak() ?: return false
        return nextToken.getType() == CommonTypes.ASSIGNMENT &&
                config.spaceAroundAssignment != null
    }

    override fun apply(
        stream: TokenStream,
        config: FormatConfig,
        state: FormatState
    ): RuleResult {
        val currentToken = stream.next()!!.token
        val spaceAround = config.spaceAroundAssignment!!

        val formattedAssignment = SpaceUtil.rebuild(
            raw = currentToken.getValue(),
            symbol = "=",
            spaceBefore = spaceAround,
            spaceAfter = spaceAround
        )

        return RuleResult(newText = formattedAssignment, state = state)
    }
}