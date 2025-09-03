package parser.command

import Token
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.isClosingParenthesis
import parser.utils.isOpeningParenthesis
import type.CommonTypes

class ParenthesisParser(private val permittedStatements: List<StatementCommand>): StatementCommand {
    override fun canHandle(token: Token?, parser: Parser): Boolean {
        if (token == null) return false
        return isOpeningParenthesis(parser) || isClosingParenthesis(parser)
    }

    override fun parse(parser: Parser): StatementResult {
        if (isClosingParenthesis(parser)) {
            throw Exception( "Unexpected closing parenthesis")
        }
        val advancedParser = parser.consume(CommonTypes.DELIMITERS).getParser()
        val statementResult = StatementParser(permittedStatements).parse(advancedParser)
        if (!statementResult.isSuccess()) {
            return statementResult
        }
        if (isOpeningParenthesis(statementResult.getParser())) {
            parse(statementResult.getParser())
        }
        if (!isClosingParenthesis(statementResult.getParser()) && !isOpeningParenthesis(parser)) {
            throw Exception( "Unexpected closing parenthesis")
        }
        return StatementBuiltResult(statementResult.getParser().advance(), statementResult.getStatement())
    }
}