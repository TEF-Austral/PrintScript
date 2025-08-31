package formatter.rules

import formatter.config.FormatConfig
import node.DeclarationStatement
import node.ASTNode

class DeclarationRule : FormatRule {
    override fun matches(node: ASTNode) = node is DeclarationStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as DeclarationStatement
        sb
            .append(" ".repeat(indentLevel * config.indentSize))
            .append("let ")
            .append(stmt.getIdentifier())

        if (config.spaceBeforeColon) sb.append(" ")
        sb.append(":")
        if (config.spaceAfterColon) sb.append(" ")
        sb.append(stmt.getDataType())

        stmt.getInitialValue()?.also { expr ->
            if (config.spaceAroundAssignment) sb.append(" = ") else sb.append("=")
            RuleRegistry.rules
                .first { it.matches(expr) }
                .apply(expr, sb, config, indentLevel)
        }

        sb.append(";").appendLine()
    }
}
