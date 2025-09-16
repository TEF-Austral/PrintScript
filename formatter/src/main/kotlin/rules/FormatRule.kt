package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode

interface FormatRule {

    fun canHandle(stream: TokenStream, config: FormatConfig): Boolean

    fun apply(stream: TokenStream, config: FormatConfig, state: FormatState): RuleResult


}
