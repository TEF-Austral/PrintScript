package parser.statement

import PrintScriptToken
import Token
import statement.enforcers.LetAssignmentEnforcer
import statement.enforcers.ColonEnforcer
import statement.enforcers.DataTypeEnforcer
import statement.enforcers.LetEnforcer
import statement.enforcers.IdentifierEnforcer
import statement.enforcers.SemanticEnforcers
import statement.enforcers.SemiColonEnforcer
import coordinates.Position
import parser.Parser
import parser.exception.ParserException
import parser.result.SemanticSuccess
import parser.result.SemanticResult
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import type.CommonTypes

class VariableDeclarationParser(
    val possibleSemanticOrders: Map<CommonTypes, SemanticEnforcers> =
        mapOf(
            CommonTypes.LET to
                LetEnforcer(
                    IdentifierEnforcer(
                        ColonEnforcer(
                            DataTypeEnforcer(
                                LetAssignmentEnforcer(
                                    SemiColonEnforcer(),
                                ),
                            ),
                        ),
                    ),
                ),
        ),
) : StatementBuilder {

    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = possibleSemanticOrders.containsKey(token?.getType())

    override fun parse(parser: Parser): StatementResult {
        val currentToken = parser.peak() ?: throwMissingTokenError(parser)
        val enforcer = possibleSemanticOrders.getValue(currentToken.getType())
        val result = executeEnforcer(enforcer, parser)
        return buildStatement(result, currentToken)
    }

    private fun executeEnforcer(
        enforcer: SemanticEnforcers,
        parser: Parser,
    ): SemanticResult {
        val emptyToken = PrintScriptToken(CommonTypes.EMPTY, "", Position(0, 0))
        val emptySemanticResult = SemanticSuccess("", emptyToken, emptyToken, null, parser)

        val result = enforcer.enforce(parser, emptySemanticResult)

        if (!result.isSuccess()) {
            throw ParserException(result.message(), result.getCoordinates())
        }

        return result
    }

    private fun buildStatement(
        result: SemanticResult,
        declarationToken: Token,
    ): StatementResult {
        val builtStatement =
            result
                .getParser()
                .getNodeBuilder()
                .buildVariableDeclarationStatementNode(
                    declarationToken,
                    result.identifier(),
                    result.dataType(),
                    result.initialValue(),
                )
        return StatementBuiltResult(result.getParser(), builtStatement)
    }

    private fun throwMissingTokenError(parser: Parser): Nothing =
        throw ParserException(
            "Expected variable declaration keyword",
            parser.peak()?.getCoordinates(),
        )
}
