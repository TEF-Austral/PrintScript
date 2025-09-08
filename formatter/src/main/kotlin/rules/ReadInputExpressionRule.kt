package formatter.rules

import formatter.config.FormatConfig
import node.ReadInputStatement
import node.ASTNode

class ReadInputExpressionRule : FormatRule {
    override fun matches(node: ASTNode) = node is ReadInputStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as ReadInputStatement
        sb.append("readInput(")
            .append("\"${stmt.printValue()}\"")
            .append(")")
    }
}
