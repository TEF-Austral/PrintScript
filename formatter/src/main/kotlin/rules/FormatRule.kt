package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode

interface FormatRule {
    val id: RuleId // each rule must expose its RuleId

    fun matches(node: ASTNode): Boolean

    fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    )
}
