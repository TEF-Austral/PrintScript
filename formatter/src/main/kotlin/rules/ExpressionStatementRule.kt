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
        RuleRegistry
            .firstMatching(stmt.getExpression(), config, RuleRegistry.rulesV10)
            .apply(stmt.getExpression(), sb, config, indentLevel)
        sb.append(";").appendLine()
    }
}
