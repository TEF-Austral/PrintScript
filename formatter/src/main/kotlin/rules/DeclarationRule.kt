package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.DeclarationStatement

class DeclarationRule(
    private val supportedKeywords: Set<String>,
    private val exprRules: List<FormatRule>,
) : FormatRule {
    override fun matches(node: ASTNode) = node is DeclarationStatement

    override val id = RuleId.Declaration

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as DeclarationStatement

        if (stmt.getKeyword() !in supportedKeywords) {
            throw UnsupportedOperationException(
                "Declarations with '${stmt.getKeyword()}' are not supported",
            )
        }

        val indent = " ".repeat(indentLevel * config.indentSize)
        sb
            .append(indent)
            .append(stmt.getKeyword())
            .append(" ")
            .append(stmt.getIdentifier())

        when (config.spaceBeforeColon) {
            true -> sb.append(" ")
            false, null -> { /* leave as-is */ }
        }

        sb.append(":")

        when (config.spaceAfterColon) {
            true -> sb.append(" ")
            false, null -> { /* leave as-is, rely on raw token value */ }
        }

        val dataTypeText =
            if (config.spaceAfterColon == null) {
                // preserve original spacing from the token (may include a leading space)
                stmt.getDataTypeToken().getValue()
            } else {
                // normalize to enum text when spacing is explicitly configured
                stmt.getDataType().toString()
            }
        sb.append(dataTypeText)

        stmt.getInitialValue()?.also { expr ->
            if (config.spaceAroundAssignment == true) {
                sb.append(" = ")
            } else if (config.spaceAroundAssignment == false) {
                sb.append("=")
            } else {
                sb.append("=") // unspecified: avoid forcing extra spaces
            }
            RuleRegistry
                .firstMatching(expr, config, exprRules)
                .apply(expr, sb, config, indentLevel)
        }

        sb.append(";").appendLine()
    }
}
