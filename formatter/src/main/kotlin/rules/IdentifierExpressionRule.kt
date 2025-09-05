package formatter.rules

import formatter.config.FormatConfig
import node.IdentifierExpression
import node.ASTNode

class IdentifierExpressionRule : FormatRule {
    override fun matches(node: ASTNode) = node is IdentifierExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        sb.append((node as IdentifierExpression).getValue())
    }
}
