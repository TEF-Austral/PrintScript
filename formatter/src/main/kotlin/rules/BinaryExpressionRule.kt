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

        RuleRegistry
            .firstMatching(expr.getLeft(), config, RuleRegistry.rulesV10)
            .apply(expr.getLeft(), sb, config, indentLevel)

        val op = expr.getOperator()
        when (config.spaceAroundOperators) {
            true -> sb.append(" ").append(op).append(" ")
            false -> sb.append(op)
            null -> {
                val raw = expr.getOperatorTokenRaw()?.getValue()
                sb.append(raw ?: op)
            }
        }

        RuleRegistry
            .firstMatching(expr.getRight(), config, RuleRegistry.rulesV10)
            .apply(expr.getRight(), sb, config, indentLevel)
    }
}
