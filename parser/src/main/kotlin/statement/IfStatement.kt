package parser.statement

import Token
import parser.Parser
import parser.result.StatementResult

class IfStatement : StatementBuilder {
    override fun parse(parser: Parser): StatementResult {
        TODO("Not yet implemented")
    }

    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean {
        TODO("Not yet implemented")
    }
}
