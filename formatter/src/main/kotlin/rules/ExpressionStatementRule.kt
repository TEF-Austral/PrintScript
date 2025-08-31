package formatter.rules

import formatter.config.FormatConfig
import node.ExpressionStatement
import node.ASTNode

class ExpressionStatementRule : FormatRule {
    override fun matches(node: ASTNode) = node is ExpressionStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as ExpressionStatement
        sb.append(" ".repeat(indentLevel * config.indentSize))
        RuleRegistry.rules
            .first { it.matches(stmt.getExpression()) }
            .apply(stmt.getExpression(), sb, config, indentLevel)
        sb.append(";").appendLine()
    }
}
