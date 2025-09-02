package parser.command

import PrintScriptToken
import Token
import command.rules.AssignmentEnforcer
import command.rules.ColonEnforcer
import command.rules.DataTypeEnforcer
import command.rules.DeclarationEnforcer
import command.rules.IdentifierEnforcer
import command.rules.SemanticEnforcers
import command.rules.SemiColonEnforcer
import coordinates.Position
import node.EmptyExpression
import parser.Parser
import parser.result.SemanticSuccess
import parser.result.StatementBuiltResult
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

    override fun parse(parser: Parser): StatementBuiltResult {
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
