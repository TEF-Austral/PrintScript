package formatter.rules

import formatter.config.FormatConfig
import node.ASTNode
import node.ReadEnvExpression

class ReadEnvExpressionRule : FormatRule {
    override fun matches(node: ASTNode) = node is ReadEnvExpression

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as ReadEnvExpression
        sb
            .append("readEnv(")
            .append("\"${stmt.envName()}\"")
            .append(")")
    }
}
