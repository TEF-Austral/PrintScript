package formatter

import TokenStream
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

    private fun format(src: TokenStream, config: FormatConfig, state: FormatState, output: StringBuilder): FormatState {
        var currentState = state

        while (!src.isAtEnd()) {
            val token = src.peak()
            if (token == null) {
                break
            }

            // Try to find a rule that can handle the current token stream state
            val applicableRule = rules.find { rule ->
                rule.canHandle(src, config)
            }

            if (applicableRule != null) {
                // Apply the rule and handle the result
                val result = applicableRule.apply(src, config, currentState)
                // Handle the rule result - update state and output based on RuleResult
                // You'll need to implement this based on your RuleResult structure
                currentState = handleRuleResult(result, currentState, output)
            } else {
                // Default behavior: consume the token and add it to output
                val streamResult = src.next()
                streamResult?.let { result ->
                    // Add basic token content to output
                    // Apply current indentation if we're at the start of a new line
                    if (currentState.isNewLine && currentState.indentationLevel > 0) {
                        val indent = " ".repeat(currentState.indentationLevel * config.indentSize)
                        output.append(indent)
                        currentState = currentState.copy(isNewLine = false)
                    }

                    output.append(token.toString())

                    // Update state based on token content
                    if (token.toString().contains('\n')) {
                        currentState = currentState.copy(isNewLine = true)
                    }
                }
            }
        }

        return currentState
    }

    private fun handleRuleResult(result: RuleResult, state: FormatState, output: StringBuilder): FormatState {
        return state
    }
}
