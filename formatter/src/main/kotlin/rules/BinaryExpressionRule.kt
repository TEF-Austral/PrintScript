package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.BinaryExpression

class BinaryExpressionRule : FormatRule {
    override val id = RuleId.BinaryExpression

    override fun matches(node: ASTNode) = node is BinaryExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val expr = node as BinaryExpression

        RuleRegistry.rulesV10
            .first { it.matches(expr.getLeft()) }
            .apply(expr.getLeft(), sb, config, indentLevel)

        val op = expr.getOperator()
        if (config.spaceAroundOperators) {
            sb.append(" ").append(op).append(" ")
        } else {
            sb.append(op)
        }

        RuleRegistry.rulesV10
            .first { it.matches(expr.getRight()) }
            .apply(expr.getRight(), sb, config, indentLevel)
    }
}
