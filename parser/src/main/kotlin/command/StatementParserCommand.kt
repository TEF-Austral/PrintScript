package parser.command

import TokenType

sealed interface StatementParserCommand : ParserCommand {

    fun canHandle(type: TokenType): Boolean

}