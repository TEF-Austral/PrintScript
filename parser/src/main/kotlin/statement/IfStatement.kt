package parser.statement

import Token
import node.Statement
import parser.Parser
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import parser.utils.checkType
import parser.utils.isClosingBrace
import parser.utils.isOpeningParenthesis
import parser.utils.isClosingParenthesis
import parser.utils.isOpeningBrace
import type.CommonTypes

class IfStatement : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = checkType(CommonTypes.CONDITIONALS, token) && token?.getValue() == "if"

    override fun parse(parser: Parser): StatementResult {
        val ifKeyword = parser.consume(CommonTypes.CONDITIONALS)
        if (!ifKeyword.isSuccess()) {
            throw Exception("Expected 'if' keyword")
        }

        val afterIf = ifKeyword.getParser()

        if (!isOpeningParenthesis(afterIf)) {
            throw Exception("Expected '(' after 'if'")
        }

        val afterOpenParen = afterIf.advance()

        val conditionResult = afterOpenParen.getExpressionParser().parseExpression(afterOpenParen)
        if (!conditionResult.isSuccess()) {
            throw Exception("Expected condition expression: ${conditionResult.message()}")
        }

        if (!isClosingParenthesis(conditionResult.getParser())) {
            throw Exception("Expected ')' after condition")
        }

        val afterCloseParen = conditionResult.getParser().advance()

        val (afterThenStatement, thenStatement) = parseBlockWithBraces(afterCloseParen, "then")

        val (finalParser, elseStatement) =
            if (checkType(CommonTypes.CONDITIONALS, afterThenStatement.peak()) &&
                afterThenStatement.peak()?.getValue() == "else"
            ) {
                val afterElse = afterThenStatement.advance()

                if (checkType(CommonTypes.CONDITIONALS, afterElse.peak()) &&
                    afterElse.peak()?.getValue() == "if"
                ) {
                    val elseIfResult = parse(afterElse)
                    if (!elseIfResult.isSuccess()) {
                        throw Exception("Error parsing else-if statement: ${elseIfResult.message()}")
                    }
                    Pair(elseIfResult.getParser(), elseIfResult.getStatement())
                } else {
                    val (afterElseStatement, elseStmt) = parseBlockWithBraces(afterElse, "else")
                    Pair(afterElseStatement, elseStmt)
                }
            } else {
                Pair(afterThenStatement, null)
            }

        val ifStatement =
            finalParser.getNodeBuilder().buildIfStatementNode(
                conditionResult.getExpression(),
                thenStatement,
                elseStatement,
                conditionResult.getExpression().getCoordinates()
            )

        return StatementBuiltResult(finalParser, ifStatement)
    }

    private fun parseBlockWithBraces(
        parser: Parser,
        blockType: String,
    ): Pair<Parser, Statement> {
        if (!isOpeningBrace(parser)) {
            throw Exception("Expected '{' to start $blockType block")
        }

        val afterOpenBrace = parser.advance()

        val statementResult = afterOpenBrace.getStatementParser().parse(afterOpenBrace)
        if (!statementResult.isSuccess()) {
            throw Exception("Error parsing $blockType statement: ${statementResult.message()}")
        }

        val afterStatement = statementResult.getParser()

        if (!isClosingBrace(afterStatement)) {
            throw Exception("Expected '}' to close $blockType block")
        }

        val afterCloseBrace = afterStatement.advance()

        return Pair(afterCloseBrace, statementResult.getStatement())
    }
}
