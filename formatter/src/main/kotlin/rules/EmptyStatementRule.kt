package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.EmptyStatement

class EmptyStatementRule : FormatRule {
    override val id = RuleId.EmptyStatement

    override fun matches(node: ASTNode): Boolean = node is EmptyStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        sb
            .append(" ".repeat(indentLevel * config.indentSize))
            .append(";")
            .appendLine()
    }
}
