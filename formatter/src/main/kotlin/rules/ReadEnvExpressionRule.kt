package formatter.rules

import formatter.config.FormatConfig
import node.ReadEnvStatement
import node.ASTNode

class ReadEnvExpressionRule : FormatRule {
    override fun matches(node: ASTNode) = node is ReadEnvStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as ReadEnvStatement
        sb.append("readEnv(")
            .append("\"${stmt.envName()}\"")
            .append(")")
    }
}
