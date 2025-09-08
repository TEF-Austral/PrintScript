package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.BlockStatement

class BlockStatementRule : FormatRule {
    override fun matches(node: ASTNode) = node is BlockStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val block = node as BlockStatement
        block.getStatements().forEach { stmt ->
            RuleRegistry.rulesV11
                .first { it.matches(stmt) }
                .apply(stmt, sb, config, indentLevel)
        }
    }
}