package formatter.rules

import formatter.config.FormatConfig
import node.LiteralExpression
import node.ASTNode

class LiteralExpressionRule : FormatRule {
    override fun matches(node: ASTNode) = node is LiteralExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int
    ) {
        sb.append((node as LiteralExpression).getValue())
    }
}
