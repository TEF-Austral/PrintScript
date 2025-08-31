package parser.command

import Token
import node.EmptyExpression
import parser.Parser
import parser.result.ExpressionBuiltResult
import parser.result.ExpressionResult
import parser.result.StatementBuiltResult
import parser.utils.checkType
import parser.utils.isValidResultAndCurrentToken
import type.CommonTypes
// TODO SEPERATE INTO PRIVATE METHODS
class VariableDeclarationParser(
    val possibleTokenTypes: List<CommonTypes> = listOf(CommonTypes.NUMBER, CommonTypes.STRING),
) : StatementParserCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.DECLARATION

    override fun parse(parser: Parser): StatementBuiltResult {
        if (parser.peak()?.getType() != CommonTypes.DECLARATION) {
            throw Exception("Expected declaration")
        }
        val identifier = parser.consume(CommonTypes.DECLARATION)
        if (!identifier.getParser().consume(CommonTypes.IDENTIFIER).isSuccess()) {
            throw Exception("Expected identifier")
        }
        val delimiterParser = identifier.getParser().consume(CommonTypes.IDENTIFIER).getParser()
        if (!delimiterParser.consume(CommonTypes.DELIMITERS).isSuccess()) {
            throw Exception("Expected delimiter")
        }
        val dataTypeParser = delimiterParser.consume(CommonTypes.DELIMITERS).getParser()

        if (!(verifyCurrentToken(possibleTokenTypes, dataTypeParser))){
            throw Exception("Expected data type")
        }

        if (!isValidResultAndCurrentToken(identifier)){
            throw Exception("Expected identifier")
        }

        val dataType = dataTypeParser.peak()!!
        val identifierToken = identifier.getParser().peak()!!

        var initialValue: ExpressionResult = ExpressionBuiltResult(dataTypeParser.advance(), EmptyExpression())

        if (checkType(CommonTypes.ASSIGNMENT, initialValue.getParser().peak())) {
            initialValue = initialValue.getParser().getExpressionParser().parseExpression(initialValue.getParser().advance())
        }

        if (!initialValue.getParser().consume(CommonTypes.DELIMITERS).isSuccess() || initialValue.getParser().peak()
                ?.getValue() != ";") {
            throw Exception("Variable declaration must end in ;")
        }

        val finalDelimiterParser = initialValue.getParser().advance() // ;
        val builtStatement = finalDelimiterParser.getNodeBuilder().buildVariableDeclarationStatementNode(identifierToken, dataType, initialValue.getExpression())
        return StatementBuiltResult(finalDelimiterParser, builtStatement)
    }

    private fun verifyCurrentToken(
        types: List<CommonTypes>,
        parser: Parser,
    ): Boolean {
        for (type in types) {
            if (checkType(type, parser.peak())) {
                return true
            }
        }
        return false
    }
}
