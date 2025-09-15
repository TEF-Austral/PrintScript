package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.ExpressionStatement

class ExpressionStatementRule : FormatRule {
    override val id = RuleId.ExpressionStatement

    override fun matches(node: ASTNode) = node is ExpressionStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as ExpressionStatement
        sb.append(" ".repeat(indentLevel * config.indentSize))
        RuleRegistry.rulesV10
            .first { it.matches(stmt.getExpression()) }
            .apply(stmt.getExpression(), sb, config, indentLevel)
        sb.append(";").appendLine()
    }
}
