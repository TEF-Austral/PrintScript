package formatter

import formatter.config.FormatConfig
import formatter.config.FormatterConstants.MAX_BLANK_LINES_BEFORE_PRINTLN
import formatter.config.FormatterConstants.MIN_BLANK_LINES_BEFORE_PRINTLN
import formatter.config.FormatterConstants.MULTI_SPACE_REGEX
import node.Program
import node.expression.LiteralExpression
import node.expression.IdentifierExpression
import node.expression.BinaryExpression
import node.expression.EmptyExpression
import node.expression.Expression
import node.statement.DeclarationStatement
import node.statement.AssignmentStatement
import node.statement.ExpressionStatement
import node.statement.PrintStatement
import node.statement.EmptyStatement
import node.statement.Statement
import java.io.Writer

class DefaultFormatter : Formatter {
    override fun formatToString(
        program: Program,
        config: FormatConfig,
    ): String {
        val sb = StringBuilder()
        val indentLevel = 0

        fun indent() = sb.append(" ".repeat(indentLevel * config.indentSize))

        fun formatExpression(expr: Expression) {
            when (expr) {
                is LiteralExpression -> sb.append(expr.getValue())
                is IdentifierExpression -> sb.append(expr.getName())
                is BinaryExpression -> {
                    formatExpression(expr.getLeft())
                    sb.append(" ${expr.getOperator().getValue()} ")
                    formatExpression(expr.getRight())
                }
                is EmptyExpression -> { /* nothing */ }
            }
        }

        fun formatStatement(stmt: Statement) {
            when (stmt) {
                is DeclarationStatement -> {
                    indent()
                    sb
                        .append("let ")
                        .append(stmt.getIdentifier())
                    if (config.spaceBeforeColon) sb.append(" ")
                    sb.append(":")
                    if (config.spaceAfterColon) sb.append(" ")
                    sb.append(stmt.getDataType())
                    stmt.getInitialValue()?.also {
                        if (config.spaceAroundAssignment) sb.append(" = ") else sb.append("=")
                        formatExpression(it)
                    }
                    sb.append(";")
                }
                is AssignmentStatement -> {
                    indent()
                    sb.append(stmt.getIdentifier())
                    if (config.spaceAroundAssignment) sb.append(" = ") else sb.append("=")
                    formatExpression(stmt.getValue())
                    sb.append(";")
                }
                is ExpressionStatement -> {
                    indent()
                    formatExpression(stmt.getExpression())
                    sb.append(";")
                }
                is PrintStatement -> {
                    val lines =
                        config.blankLinesBeforePrintln.coerceIn(
                            MIN_BLANK_LINES_BEFORE_PRINTLN,
                            MAX_BLANK_LINES_BEFORE_PRINTLN,
                        )
                    repeat(lines) { sb.appendLine() }
                    indent()
                    sb.append("println(")
                    formatExpression(stmt.getExpression())
                    sb.append(");")
                }
                is EmptyStatement -> {
                    indent()
                    sb.append(";")
                }
            }
            sb.appendLine()
        }

        program.getStatements().forEach { formatStatement(it) }

        return sb
            .toString()
            .lines()
            .joinToString("\n") { line ->
                val indent = line.takeWhile { it == ' ' }
                val content = line.drop(indent.length).replace(MULTI_SPACE_REGEX, " ")
                indent + content
            } + "\n"
    }

    override fun formatToWriter(
        program: Program,
        config: FormatConfig,
        writer: Writer,
    ) {
        writer.write(formatToString(program, config))
    }
}
