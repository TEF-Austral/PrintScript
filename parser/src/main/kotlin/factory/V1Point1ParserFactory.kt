package parser.factory

import Token
import builder.NodeBuilder
import parser.Parser
import parser.ParserInterface
import parser.statement.AssignmentParser
import parser.statement.ExpressionParser
import parser.statement.IfStatement
import parser.statement.PrintParser
import parser.statement.StatementParser
import parser.statement.VariableDeclarationParser
import parser.statement.binary.DefaultParseBinary
import parser.statement.expression.DelimitersBuilder
import parser.statement.expression.ExpressionParsingBuilder
import parser.statement.expression.ExpressionRegistry
import parser.statement.expression.IdentifierBuilder
import parser.statement.expression.LiteralBuilder
import statement.enforcers.LetAssignmentEnforcer
import statement.enforcers.ColonEnforcer
import statement.enforcers.ConstAssignmentEnforcer
import statement.enforcers.ConstEnforcer
import statement.enforcers.DataTypeEnforcer
import statement.enforcers.IdentifierEnforcer
import statement.enforcers.LetEnforcer
import statement.enforcers.SemiColonEnforcer
import type.CommonTypes

class V1Point1ParserFactory : ParserFactory {
    override fun createParser(
        tokens: List<Token>,
        nodeBuilder: NodeBuilder,
    ): Parser {
        val tokenToExpression =
            ExpressionRegistry(
                listOf(
                    IdentifierBuilder(),
                    LiteralBuilder(listOf(CommonTypes.NUMBER_LITERAL, CommonTypes.STRING_LITERAL, CommonTypes.BOOLEAN_LITERAL)),
                    DelimitersBuilder(),
                ),
            )
        val parseBinary =
            DefaultParseBinary(
                ExpressionRegistry(
                    listOf(
                        IdentifierBuilder(),
                        LiteralBuilder(listOf(CommonTypes.NUMBER_LITERAL, CommonTypes.STRING_LITERAL, CommonTypes.BOOLEAN_LITERAL)),
                        DelimitersBuilder(),
                    ),
                ),
            )
        val expressionParser = ExpressionParsingBuilder(tokenToExpression, parseBinary)
        val statementParser =
            StatementParser(
                listOf(
                    VariableDeclarationParser(LetEnforcer(IdentifierEnforcer(ColonEnforcer(DataTypeEnforcer(LetAssignmentEnforcer(SemiColonEnforcer()), listOf(CommonTypes.NUMBER, CommonTypes.STRING, CommonTypes.BOOLEAN)))))),
                    VariableDeclarationParser(ConstEnforcer(IdentifierEnforcer(ConstAssignmentEnforcer(SemiColonEnforcer())))),
                    VariableDeclarationParser(ConstEnforcer(IdentifierEnforcer(ColonEnforcer(DataTypeEnforcer(ConstAssignmentEnforcer(SemiColonEnforcer()), listOf(CommonTypes.NUMBER, CommonTypes.STRING, CommonTypes.BOOLEAN)))))),
                    PrintParser(),
                    AssignmentParser(),
                    ExpressionParser(),
                    IfStatement(),
                ),
            )
        return Parser(tokens, nodeBuilder, expressionParser, statementParser)
    }

    override fun withNewTokens(
        tokens: List<Token>,
        parser: ParserInterface,
    ): Parser = Parser(tokens, parser.getNodeBuilder(), parser.getExpressionParser(), parser.getStatementParser(), parser.getCurrent())
}
