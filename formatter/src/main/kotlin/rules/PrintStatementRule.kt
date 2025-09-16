// Kotlin
package formatter.rules

import formatter.config.FormatConfig
import formatter.config.FormatterConstants.MAX_BLANK_LINES_AFTER_PRINTLN
import formatter.config.FormatterConstants.MIN_BLANK_LINES_AFTER_PRINTLN
import node.ASTNode
import node.PrintStatement

class PrintStatementRule : FormatRule {
    override val id = RuleId.PrintStatement

    override fun matches(node: ASTNode) = node is PrintStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int,
    ) {
        val stmt = node as PrintStatement
        val indent = " ".repeat(indentLevel * config.indentSize)

        if (config.enforceSingleSpace == true) {
            sb.append(indent).append("println ( ")
            RuleRegistry
                .firstMatching(stmt.getExpression(), config, RuleRegistry.rulesV10)
                .apply(stmt.getExpression(), sb, config, indentLevel)
            sb.append(" );").appendLine()
        } else {
            sb.append(indent).append("println(")
            RuleRegistry
                .firstMatching(stmt.getExpression(), config, RuleRegistry.rulesV10)
                .apply(stmt.getExpression(), sb, config, indentLevel)
            sb.append(");").appendLine()
        }

        val lines =
            (config.blankLinesAfterPrintln).coerceIn(
                MIN_BLANK_LINES_AFTER_PRINTLN,
                MAX_BLANK_LINES_AFTER_PRINTLN,
            )
        repeat(lines) { sb.appendLine() }
    }
}
