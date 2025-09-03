package parser.command

import PrintScriptToken
import Token
import command.enforcers.AssignmentEnforcer
import command.enforcers.ColonEnforcer
import command.enforcers.DataTypeEnforcer
import command.enforcers.DeclarationEnforcer
import command.enforcers.IdentifierEnforcer
import command.enforcers.SemanticEnforcers
import command.enforcers.SemiColonEnforcer
import coordinates.Position
import node.EmptyExpression
import parser.Parser
import parser.result.SemanticSuccess
import parser.result.StatementBuiltResult
import parser.result.StatementResult
import type.CommonTypes

class VariableDeclarationParser(
    val semanticOrder: SemanticEnforcers =
        DeclarationEnforcer(
            IdentifierEnforcer(
                ColonEnforcer(
                    DataTypeEnforcer(
                        AssignmentEnforcer(
                            SemiColonEnforcer(),
                        ),
                    ),
                ),
            ),
        ),
) : StatementCommand {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.DECLARATION

    override fun parse(parser: Parser): StatementResult {
        val emptyToken = PrintScriptToken(CommonTypes.EMPTY, "", Position(0, 0))
        val emptySemanticResult = SemanticSuccess("", emptyToken, emptyToken, EmptyExpression(), parser)
        val result = semanticOrder.enforce(emptySemanticResult)
        if (!result.isSuccess()) {
            throw Exception("Semantic error: ${result.message()}")
        }
        val builtStatement =
            result.getParser().getNodeBuilder().buildVariableDeclarationStatementNode(
                result.identifier(),
                result.dataType(),
                result.initialValue(),
            )
        return StatementBuiltResult(result.getParser(), builtStatement)
    }
}
