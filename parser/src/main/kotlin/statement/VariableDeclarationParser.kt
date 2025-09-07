package parser.statement

import PrintScriptToken
import Token
import statement.enforcers.AssignmentEnforcer
import statement.enforcers.ColonEnforcer
import statement.enforcers.DataTypeEnforcer
import statement.enforcers.DeclarationEnforcer
import statement.enforcers.IdentifierEnforcer
import statement.enforcers.SemanticEnforcers
import statement.enforcers.SemiColonEnforcer
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
) : StatementBuilder {
    override fun canHandle(
        token: Token?,
        parser: Parser,
    ): Boolean = token?.getType() == CommonTypes.LET // TODO CHEQUEUAR EL CONST

    override fun parse(parser: Parser): StatementResult {
        val emptyToken = PrintScriptToken(CommonTypes.EMPTY, "", Position(0, 0))
        val emptySemanticResult = SemanticSuccess("", emptyToken, emptyToken, EmptyExpression(), parser)
        val result = semanticOrder.enforce(emptySemanticResult)
        if (!result.isSuccess()) {
            throw Exception("Semantic error: ${result.message()} ${result.identifier()} ${result.dataType()}")
        }
        val builtStatement =
            result.getParser().getNodeBuilder().buildVariableDeclarationStatementNode(
                PrintScriptToken(CommonTypes.LET, "let", Position(0, 0)), // TODO Verificar Esto EL CONST
                result.identifier(),
                result.dataType(),
                result.initialValue(),
            )
        return StatementBuiltResult(result.getParser(), builtStatement)
    }
}
