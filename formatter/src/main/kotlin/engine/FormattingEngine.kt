package formatter.engine

import TokenStream
import formatter.config.FormatConfig
import type.CommonTypes

class FormattingEngine(
    private val rules: List<Rule>,
    private val postProcessors: List<LinePostProcessor>,
) {
    fun format(
        src: TokenStream,
        config: FormatConfig,
    ): String {
        val ctx = FormatterContext(config, StringBuilder())

        var currentStream = src
        while (!currentStream.isAtEnd()) {
            val result = currentStream.next() ?: break
            val token = result.token
            currentStream = result.nextStream

            if (token.getType() == CommonTypes.EMPTY) continue

            ctx.isEndAfterThisToken = { currentStream.isAtEnd() }
            ctx.ensureIndentBeforeNonClosing(token)

            val rule =
                rules.firstOrNull { it.applies(token, ctx) }
                    ?: error("No rule matched token: ${token.getValue()}")

            rule.apply(token, ctx)
            ctx.previousToken = token
        }

        var output = ctx.out.toString()
        postProcessors.forEach { pp -> output = pp.process(output, config) }
        return output.trimEnd()
    }
}
