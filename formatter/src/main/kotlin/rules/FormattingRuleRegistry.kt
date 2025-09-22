import formatter.rules.FormattingRule
import formatter.engine.FormatterContext
import formatter.rules.SingleSpaceEnforcer

class FormattingRuleRegistry(
    private val rules: List<FormattingRule>,
) : FormattingRule {

    override fun canHandle(
        token: Token,
        context: FormatterContext,
    ): Boolean = findApplicableRule(token, context) != null

    override fun apply(
        token: Token,
        context: FormatterContext,
    ): Pair<String, FormatterContext> {
        val rule = findApplicableRule(token, context)
        val (rawText, nextCtx) = rule?.apply(token, context) ?: Pair(token.getValue(), context)
        val enforcedText = SingleSpaceEnforcer.enforce(token, context, rawText)
        return Pair(enforcedText, nextCtx)
    }

    private fun findApplicableRule(
        token: Token,
        context: FormatterContext,
    ): FormattingRule? = rules.firstOrNull { it.canHandle(token, context) }
}
