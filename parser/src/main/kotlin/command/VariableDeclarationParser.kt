package parser.command

import Token
import TokenType
import node.expression.Expression
import node.statement.Statement
import parser.Parser
import parser.result.ParseResult

class VariableDeclarationParser : StatementParserCommand {

    override fun canHandle(token: Token?, parser: Parser): Boolean {
        return token?.getType() == TokenType.DECLARATION
    }

    override fun parse(parser: Parser): ParseResult<Statement> {
        // 'let' or 'const'
        when (val declRes = parser.consumeOrError(TokenType.DECLARATION)) {
            is ParseResult.Failure -> return declRes
            else -> {}
        }
        // identifier
        val idToken = when (val idRes = parser.consumeOrError(TokenType.IDENTIFIER)) {
            is ParseResult.Failure -> return idRes
            is ParseResult.Success -> idRes.value
        }
        // ':'
        when (val colonRes = parser.consumeOrError(TokenType.DELIMITERS)) {
            is ParseResult.Failure -> return colonRes
            else -> {}
        }
        // data type
        val dataTypeToken = when (val dtRes = parser.consumeOrError(TokenType.DATA_TYPES)) {
            is ParseResult.Failure -> return dtRes
            is ParseResult.Success -> dtRes.value
        }
        // optional initialization
        var initialValue: Expression? = null
        if (parser.match(TokenType.ASSIGNMENT)) {
            initialValue = parser.getExpressionParser().parseExpression(parser)
        }
        // ';'
        when (val semiRes = parser.consumeOrError(TokenType.DELIMITERS)) {
            is ParseResult.Failure -> return semiRes
            else -> {}
        }
        return ParseResult.Success(
            parser.getNodeBuilder().buildVariableDeclarationStatementNode(
                idToken, dataTypeToken, initialValue
            )
        )
    }
}