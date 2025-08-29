package formatter.visitor

import formatter.config.FormatConfig
import node.Program
import node.expression.BinaryExpression
import node.expression.EmptyExpression
import node.expression.Expression
import node.expression.IdentifierExpression
import node.expression.LiteralExpression
import node.statement.AssignmentStatement
import node.statement.DeclarationStatement
import node.statement.EmptyStatement
import node.statement.ExpressionStatement
import node.statement.PrintStatement
import node.statement.Statement

class ASTFormatterVisitor {
    private val sb = StringBuilder()
    private var indentLevel = 0

    private fun appendIndent(cfg: FormatConfig) {
        sb.append(" ".repeat(indentLevel * cfg.indentSize))
    }

    fun visitProgram(program: Program, cfg: FormatConfig): String {
        for (stmt in program.getStatements()) {
            if (stmt is PrintStatement) {
                val lines = cfg.blankLinesBeforePrintln.coerceIn(0, 2)
                repeat(lines) { sb.appendLine() }
            }
            appendIndent(cfg)
            visitStatement(stmt, cfg)
        }
        return sb.toString()
    }

    private fun visitStatement(stmt: Statement, config: FormatConfig) {
        when (stmt) {
            is DeclarationStatement -> visitDeclaration(stmt, config)
            is AssignmentStatement -> visitAssignment(stmt, config)
            is ExpressionStatement -> visitExpression(stmt.getExpression(), config).also { sb.append(";") }
            is PrintStatement -> visitPrint(stmt, config)
            is EmptyStatement -> sb.appendLine(";")
        }
        sb.appendLine()
    }

    private fun visitDeclaration(stmt: DeclarationStatement, cfg: FormatConfig) {
        sb.append("let ")
        sb.append(stmt.getIdentifier())
        if (cfg.spaceBeforeColon) sb.append(" ")
        sb.append(":")
        if (cfg.spaceAfterColon) sb.append(" ")
        sb.append(stmt.getDataType())
        stmt.getInitialValue()?.let {
            if (cfg.spaceAroundAssignment) sb.append(" = ") else sb.append("=")
            sb.append(visitExpression(it, cfg))
        }
        sb.append(";")
    }

    private fun visitAssignment(stmt: AssignmentStatement, cfg: FormatConfig) {
        sb.append(stmt.getIdentifier())
        if (cfg.spaceAroundAssignment) sb.append(" = ") else sb.append("=")
        sb.append(visitExpression(stmt.getValue(), cfg))
        sb.append(";")
    }

    private fun visitPrint(stmt: PrintStatement, cfg: FormatConfig) {
        sb.append("println(")
        sb.append(visitExpression(stmt.getExpression(), cfg))
        sb.append(");")
    }

    private fun visitExpression(expr: Expression, cfg: FormatConfig): String =
        when (expr) {
            is LiteralExpression -> expr.getValue()
            is IdentifierExpression -> expr.getName()
            is BinaryExpression -> {
                val left  = visitExpression(expr.getLeft(), cfg)
                val op    = expr.getOperator().getValue()
                val right = visitExpression(expr.getRight(), cfg)
                "$left $op $right"
            }
            is EmptyExpression -> ""
        }

}