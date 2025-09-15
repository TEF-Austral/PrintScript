package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.IfStatement
import kotlin.apply

class IfStatementRule : FormatRule {
    override val id = RuleId.IfStatement

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
        RuleRegistry
            .firstMatching(stmt.getCondition(), config, RuleRegistry.rulesV11)
            .apply(stmt.getCondition(), sb, config, indentLevel)

        if (config.ifBraceOnSameLine == true) {
            sb.append(") {").appendLine()
        } else if (config.ifBraceOnSameLine == false) {
            sb.append(")").appendLine()
            sb.append(indent).append("{").appendLine()
        } else {
            // unspecified: keep same-line default behavior (fallback to previous default)
            sb.append(") {").appendLine()
        }

        // consequence
        RuleRegistry
            .firstMatching(stmt.getConsequence(), config, RuleRegistry.rulesV11)
            .apply(stmt.getConsequence(), sb, config, bodyIndent)

        // closing
        sb.append(indent).append("}")
        if (stmt.hasAlternative()) {
            if (config.ifBraceOnSameLine == true) {
                sb.append(" else ").appendLine("{")
            } else if (config.ifBraceOnSameLine == false) {
                sb.append(" else ").appendLine()
            } else {
                sb.append(" else ").appendLine("{")
            }

            RuleRegistry
                .firstMatching(stmt.getAlternative()!!, config, RuleRegistry.rulesV11)
                .apply(stmt.getAlternative()!!, sb, config, bodyIndent)
            sb.append(indent).append("}")
        }
        sb.appendLine()
    }
}
