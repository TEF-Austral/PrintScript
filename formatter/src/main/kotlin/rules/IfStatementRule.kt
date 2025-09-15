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
        val bodyIndent = indentLevel + config.ifIndentInside

        // opening
        sb.append(indent).append("if (")
        RuleRegistry.rulesV11
            .first { it.matches(stmt.getCondition()) }
            .apply(stmt.getCondition(), sb, config, indentLevel)

        if (config.ifBraceOnSameLine) {
            sb.append(") {").appendLine()
        } else {
            sb.append(")").appendLine()
            sb.append(indent).append("{").appendLine()
        }

        // consequence
        RuleRegistry.rulesV11
            .first { it.matches(stmt.getConsequence()) }
            .apply(stmt.getConsequence(), sb, config, bodyIndent)

        // closing
        sb.append(indent).append("}")
        if (stmt.hasAlternative()) {
            sb.append(" else ").appendLine(if (config.ifBraceOnSameLine) "{" else "")
            RuleRegistry.rulesV11
                .first { it.matches(stmt.getAlternative()!!) }
                .apply(stmt.getAlternative()!!, sb, config, bodyIndent)
            sb.append(indent).append("}")
        }
        sb.appendLine()
    }
}
