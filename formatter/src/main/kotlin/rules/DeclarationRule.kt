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

        val rawColon = stmt.getColonToken()?.getValue() ?: ":"
        val colonText =
            if (config.spaceBeforeColon == null && config.spaceAfterColon == null) {
                // both sides: preserve raw
                rawColon
            } else {
                // per side: null => preserve that side, true/false => enforce
                SpacingUtil.rebuild(rawColon, ":", config.spaceBeforeColon, config.spaceAfterColon)
            }
        sb.append(colonText)

        // CAMBIO REALIZADO AQUÍ:
        // Se eliminó el bloque if/else. Como `colonText` ya maneja todo el espaciado
        // alrededor de los dos puntos, simplemente agregamos el nombre del tipo de dato.
        sb.append(stmt.getDataType().toString())

        stmt.getInitialValue()?.also { expr ->
            when (config.spaceAroundAssignment) {
                true -> sb.append(" = ")
                false -> sb.append("=")
                null -> {
                    val raw = stmt.getAssignmentToken()?.getValue() ?: "="
                    sb.append(SpacingUtil.rebuild(raw, "=", null, null))
                }
            }
            RuleRegistry
                .firstMatching(expr, config, exprRules)
                .apply(expr, sb, config, indentLevel)
        }

        sb.append(";").appendLine()
    }
}