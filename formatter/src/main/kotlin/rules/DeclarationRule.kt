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

        val bothColonNull = (config.spaceBeforeColon == null && config.spaceAfterColon == null)
        if (bothColonNull) {
            // Prefer original colon token (may carry spacing), else bare ':'
            val rawColon = stmt.getColonToken()?.getValue() ?: ":"
            sb.append(rawColon)
        } else {
            if (config.spaceBeforeColon == true) sb.append(" ")
            sb.append(":")
            if (config.spaceAfterColon == true) sb.append(" ")
        }

        val typeText =
            if (bothColonNull) stmt.getDataTypeToken().getValue() else stmt.getDataType().toString()
        sb.append(typeText)

        stmt.getInitialValue()?.also { expr ->
            when (config.spaceAroundAssignment) {
                true -> sb.append(" = ")
                false -> sb.append("=")
                null -> {
                    // Prefer original token (may include spacing), else bare '='
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
