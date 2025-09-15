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

        sb
            .append(" ".repeat(indentLevel * config.indentSize))
            .append(stmt.getIdentifier())

        if (config.spaceAroundAssignment) sb.append(" = ") else sb.append("=")

        RuleRegistry.rulesV10
            .first { it.matches(stmt.getValue()) }
            .apply(stmt.getValue(), sb, config, indentLevel)

        sb.append(";").appendLine()
    }
}
