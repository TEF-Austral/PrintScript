package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.IfStatement

class IfStatementRule : FormatRule {
    override fun matches(node: ASTNode) = node is IfStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as IfStatement
        val indent = " ".repeat(indentLevel * config.indentSize)

        // if(<condition>) {
        sb.append(indent)
            .append("if(")
        RuleRegistry.rulesV11
            .first { it.matches(stmt.getCondition()) }
            .apply(stmt.getCondition(), sb, config, indentLevel)
        sb.append(") {").appendLine()

        // consequence
        RuleRegistry.rulesV11
            .first { it.matches(stmt.getConsequence()) }
            .apply(stmt.getConsequence(), sb, config, indentLevel + 1)

        // close block
        sb.append(indent).append("}")

        // else
        if (stmt.hasAlternative()) {
            sb.append(" else {").appendLine()
            RuleRegistry.rulesV11
                .first { it.matches(stmt.getAlternative()!!) }
                .apply(stmt.getAlternative()!!, sb, config, indentLevel + 1)
            sb.append(indent).append("}")
        }
        sb.appendLine()
    }
}
