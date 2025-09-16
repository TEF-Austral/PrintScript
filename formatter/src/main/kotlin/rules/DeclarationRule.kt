// file: 'formatter/src/main/kotlin/rules/DeclarationRule.kt'
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
            false, null -> { /* keep as-is */ }
        }

        sb.append(":")

        when (config.spaceAfterColon) {
            true -> sb.append(" ")
            false, null -> { /* keep as-is */ }
        }

        val dataTypeText =
            if (config.spaceAfterColon == null) {
                stmt.getDataTypeToken().getValue()
            } else {
                stmt.getDataType().toString()
            }
        sb.append(dataTypeText)

        stmt.getInitialValue()?.also { expr ->
            when (config.spaceAroundAssignment) {
                true -> sb.append(" = ")
                false -> sb.append("=")
                null -> {
                    // preserve original spacing if available; otherwise fall back to bare '='
                    val raw = stmt.getAssignmentToken()?.getValue() ?: "="
                    sb.append(raw)
                }
            }
            RuleRegistry
                .firstMatching(expr, config, exprRules)
                .apply(expr, sb, config, indentLevel)
        }

        sb.append(";").appendLine()
    }
}
