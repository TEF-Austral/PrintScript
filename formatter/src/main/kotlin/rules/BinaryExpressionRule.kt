package formatter.rules

import formatter.config.FormatConfig
import node.BinaryExpression
import node.ASTNode

class BinaryExpressionRule : FormatRule {
    override fun matches(node: ASTNode) = node is BinaryExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val expr = node as BinaryExpression

        RuleRegistry.rules
            .first { it.matches(expr.getLeft()) }
            .apply(expr.getLeft(), sb, config, indentLevel)

        sb.append(" ${expr.getOperator().getValue()} ")

        RuleRegistry.rules
            .first { it.matches(expr.getRight()) }
            .apply(expr.getRight(), sb, config, indentLevel)
    }
}
