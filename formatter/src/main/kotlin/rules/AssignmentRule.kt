package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.AssignmentStatement

class AssignmentRule : FormatRule {
    override val id = RuleId.Assignment

    override fun matches(node: ASTNode) = node is AssignmentStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as AssignmentStatement
        sb.append(" ".repeat(indentLevel * config.indentSize)).append(stmt.getIdentifier())

        when (config.spaceAroundAssignment) {
            true -> sb.append(" = ")
            false -> sb.append("=")
            null -> {
                val raw = stmt.getAssignmentToken()?.getValue() ?: "="
                sb.append(SpacingUtil.rebuild(raw, "=", null, null))
            }
        }

        RuleRegistry
            .firstMatching(stmt.getValue(), config, RuleRegistry.rulesV10)
            .apply(stmt.getValue(), sb, config, indentLevel)

        sb.append(";").appendLine()
    }
}
