//package formatter.rules
//
//import Token
//import TokenStream
//import formatter.config.FormatConfig
//import type.CommonTypes
//
//class IfIndentRule : FormatRule {
//
//    override fun canHandle(tokenStream: TokenStream, config: FormatConfig): Boolean {
//        val currentToken = tokenStream.peak()
//        val state = tokenStream.getCurrentState()
//
//        // Handle content inside if blocks that needs indentation
//        return state.insideIfBlock && state.isNewLine
//    }
//
//    override fun apply(
//        tokenStream: TokenStream,
//        config: FormatConfig,
//        state: FormatState
//    ): RuleResult {
//        // Calculate the indent for content inside the if block
//        val baseIndent = (state.indentationLevel - config.ifIndentInside) * config.indentSize
//        val ifIndent = config.ifIndentInside * config.indentSize
//        val totalIndent = baseIndent + ifIndent
//
//        val indent = " ".repeat(totalIndent)
//
//        val newState = state.copy(isNewLine = false)
//
//        return RuleResult(indent, newState)
//    }
//}