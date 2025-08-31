package formatter.rules

import formatter.config.FormatConfig
import formatter.config.FormatterConstants.MAX_BLANK_LINES_BEFORE_PRINTLN
import formatter.config.FormatterConstants.MIN_BLANK_LINES_BEFORE_PRINTLN
import node.PrintStatement
import node.ASTNode

class PrintStatementRule : FormatRule {
    override fun matches(node: ASTNode) = node is PrintStatement

    override fun apply(
        node: ASTNode,
        sb: StringBuilder,
        config: FormatConfig,
        indentLevel: Int
    ) {
        val stmt = node as PrintStatement
        val lines = config.blankLinesBeforePrintln
            .coerceIn(MIN_BLANK_LINES_BEFORE_PRINTLN, MAX_BLANK_LINES_BEFORE_PRINTLN)
        repeat(lines) { sb.appendLine() }

        sb.append(" ".repeat(indentLevel * config.indentSize))
            .append("println(")

        RuleRegistry.rules.first { it.matches(stmt.getExpression()) }
            .apply(stmt.getExpression(), sb, config, indentLevel)

        sb.append(");").appendLine()
    }
}
