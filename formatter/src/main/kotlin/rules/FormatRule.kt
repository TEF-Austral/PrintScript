package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode

interface FormatRule {
    fun matches(node: ASTNode): Boolean

    fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    )
}
