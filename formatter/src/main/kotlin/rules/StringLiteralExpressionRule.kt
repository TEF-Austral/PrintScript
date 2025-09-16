package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.LiteralExpression
import type.CommonTypes

class StringLiteralExpressionRule : FormatRule {
    override val id = RuleId.StringLiteralExpression

    override fun matches(node: ASTNode) = node is LiteralExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val literal = node as LiteralExpression
        val value =
            if (literal.getType() == CommonTypes.STRING_LITERAL) {
                "\"${literal.getValue()}\""
            } else {
                literal.getValue()
            }
        sb.append(value)
    }
}
