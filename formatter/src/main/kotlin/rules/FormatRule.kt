package formatter.rules

import TokenStream
import formatter.config.FormatConfig

interface FormatRule {

    fun canHandle(stream: TokenStream, config: FormatConfig): Boolean

    fun apply(stream: TokenStream, config: FormatConfig, state: FormatState): RuleResult


}
