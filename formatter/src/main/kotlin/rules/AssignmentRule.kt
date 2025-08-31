package formatter.rules

import formatter.config.FormatConfig
import node.AssignmentStatement
import node.ASTNode

class AssignmentRule : FormatRule {
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
        RuleRegistry.rules
            .first { it.matches(stmt.getValue()) }
            .apply(stmt.getValue(), sb, config, indentLevel)

        sb.append(";").appendLine()
    }
}
