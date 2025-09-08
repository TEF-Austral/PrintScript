package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.DeclarationStatement

class DeclarationRule(
    private val supportedKeywords: Set<String>,
    private val exprRules: List<FormatRule>
) : FormatRule {
    override fun matches(node: ASTNode) = node is DeclarationStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int
    ) {
        val stmt = node as DeclarationStatement

        if (stmt.getKeyword() !in supportedKeywords) {
            throw UnsupportedOperationException(
                "Declarations with '${stmt.getKeyword()}' are not supported"
            )
        }

        val indent = " ".repeat(indentLevel * config.indentSize)
        sb.append(indent)
            .append(stmt.getKeyword())
            .append(" ")
            .append(stmt.getIdentifier())

        if (config.spaceBeforeColon) sb.append(" ")
        sb.append(":")
        if (config.spaceAfterColon) sb.append(" ")
        sb.append(stmt.getDataType())

        stmt.getInitialValue()?.also { expr ->
            if (config.spaceAroundAssignment) sb.append(" = ") else sb.append("=")
            exprRules.first { it.matches(expr) }
                .apply(expr, sb, config, indentLevel)
        }

        sb.append(";").appendLine()
    }
}
