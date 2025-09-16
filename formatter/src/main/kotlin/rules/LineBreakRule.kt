//package formatter.rules
//
//import Token
//import formatter.config.FormatConfig
//import formatter.config.FormatterConstants
//import type.CommonTypes
//
//class LineBreakRule : FormatRule {
//
//    override fun canHandle(token: Token, config: FormatConfig): Boolean {
//        return config.breakAfterStatement != null || config.blankLinesAfterPrintln > 0
//    }
//
//    override fun apply(
//        currentToken: Token,
//        previousToken: Token?,
//        config: FormatConfig,
//        state: FormatState
//    ): RuleResult {
//        var prefix = ""
//        var nextState = state
//
//        // Handle break after statement (semicolon)
//        if (previousToken?.getValue() == ";" && config.breakAfterStatement == true) {
//            prefix = "\n"
//            nextState = state.copy(isNewLine = true)
//        }
//
//        // Handle blank lines after println
//        if (previousToken?.getType() == CommonTypes.PRINT && config.blankLinesAfterPrintln > 0) {
//            val blanks = config.blankLinesAfterPrintln.coerceIn(
//                FormatterConstants.MIN_BLANK_LINES_AFTER_PRINTLN,
//                FormatterConstants.MAX_BLANK_LINES_AFTER_PRINTLN
//            )
//            prefix = "\n".repeat(blanks + 1) // +1 for the line break itself
//            nextState = state.copy(isNewLine = true)
//        }
//
//        return RuleResult(prefix, nextState)
//    }
//}