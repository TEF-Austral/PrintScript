package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.IfStatement

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

        // opening "if(" header: null => keep original header spacing if available
        val header = stmt.getIfHeaderToken()?.getValue() ?: "if ("
        sb.append(indent).append(header)

        // condition
        RuleRegistry
            .firstMatching(stmt.getCondition(), config, RuleRegistry.rulesV11)
            .apply(stmt.getCondition(), sb, config, indentLevel)

        // decide brace placement: null => use original if provided
        val sameLine = config.ifBraceOnSameLine ?: stmt.isBracesOnSameLine() ?: true
        val closeParenRaw = stmt.getCloseParenToken()?.getValue() ?: ")"

        if (sameLine) {
            // null => we already preserved close paren spacing via closeParenRaw
            sb.append(closeParenRaw).append(" {").appendLine()
        } else {
            sb.append(closeParenRaw.trimEnd()).appendLine()
            sb.append(indent).append("{").appendLine()
        }

        // consequence
        RuleRegistry
            .firstMatching(stmt.getConsequence(), config, RuleRegistry.rulesV11)
            .apply(stmt.getConsequence(), sb, config, bodyIndent)

        // closing and optional else
        sb.append(indent).append("}")
        if (stmt.hasAlternative()) {
            if (sameLine) {
                val elseLex = stmt.getElseTokenRaw()?.getValue() ?: " else "
                sb.append(elseLex).appendLine("{")
            } else {
                val elseLex = stmt.getElseTokenRaw()?.getValue()?.trim() ?: "else"
                sb.append(" ").append(elseLex).appendLine()
                sb.append(indent).append("{").appendLine()
            }

            RuleRegistry
                .firstMatching(stmt.getAlternative()!!, config, RuleRegistry.rulesV11)
                .apply(stmt.getAlternative()!!, sb, config, bodyIndent)

            sb.append(indent).append("}")
        }
        sb.appendLine()
    }
}
