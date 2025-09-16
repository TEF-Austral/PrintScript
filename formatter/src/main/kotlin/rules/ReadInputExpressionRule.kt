package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.ReadInputExpression

class ReadInputExpressionRule : FormatRule {
    override val id = RuleId.ReadInputExpression

    override fun matches(node: ASTNode) = node is ReadInputExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as ReadInputExpression
        sb
            .append("readInput(")
            .append("\"${stmt.printValue()}\"")
            .append(")")
    }
}
