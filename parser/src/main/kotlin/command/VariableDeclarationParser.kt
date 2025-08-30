package parser.command

import Token
import node.Expression
import node.Statement
import parser.Parser
import parser.result.ParseResult
import type.CommonTypes

class VariableDeclarationParser : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.DECLARATION

    override fun parse(parser: Parser): Pair<ParseResult<Statement>, Parser> {
        var p = parser

        // 'let' or 'const'
        val (declRes, p1) = p.consumeOrError(CommonTypes.DECLARATION)
        if (declRes is ParseResult.Failure) return Pair(declRes, p1)
        p = p1

        // identifier
        val (idRes, p2) = p.consumeOrError(CommonTypes.IDENTIFIER)
        if (idRes is ParseResult.Failure) return Pair(idRes, p2)
        val idToken = (idRes as ParseResult.Success).value
        p = p2

        // ':'
        val (colonRes, p3) = p.consumeOrError(CommonTypes.DELIMITERS)
        if (colonRes is ParseResult.Failure) return Pair(colonRes, p3)
        p = p3

        // data type
        val (dtRes, p4) = p.consumeOrError(CommonTypes.DATA_TYPES)
        if (dtRes is ParseResult.Failure) return Pair(dtRes, p4)
        val dataTypeToken = (dtRes as ParseResult.Success).value
        p = p4

        // optional initialization
        var initialValue: Expression? = null
        if (p.getCurrentToken()?.getType() == CommonTypes.ASSIGNMENT) {
            val (assignRes, p5) = p.consumeOrError(CommonTypes.ASSIGNMENT)
            if (assignRes is ParseResult.Failure) return Pair(assignRes, p5)
            p = p5

            val (expr, p6) = p.getExpressionParser().parseExpression(p)
            initialValue = expr
            p = p6
        }

        // ';'
        val (semiRes, p7) = p.consumeOrError(CommonTypes.DELIMITERS)
        if (semiRes is ParseResult.Failure) return Pair(semiRes, p7)
        p = p7

        val stmt =
            p
                .getNodeBuilder()
                .buildVariableDeclarationStatementNode(idToken, dataTypeToken, initialValue)
        return Pair(ParseResult.Success(stmt), p)
    }
}
