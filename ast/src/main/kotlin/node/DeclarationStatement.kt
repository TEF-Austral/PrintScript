package node

import Token
import coordinates.Coordinates
import type.CommonTypes

class DeclarationStatement(
    private val declarationType: Token,
    private val identifier: Token,
    private val dataType: Token,
    private val initialValue: Expression? = null,
    private val coordinates: Coordinates,
    private val assignmentOperator: Token? = null, // optional, may contain raw spacing like " = "
    private val colonToken: Token? = null, // optional, may contain raw spacing like ": " or " :"
) : Statement {
    fun getDeclarationType(): CommonTypes = declarationType.getType()

    fun getIdentifier(): String = identifier.getValue()

    fun getDataType(): CommonTypes = dataType.getType()

    fun getInitialValue(): Expression? = initialValue

    fun getIdentifierToken(): Token = identifier

    fun getDataTypeToken(): Token = dataType

    fun getKeyword(): String = declarationType.getValue()

    fun getAssignmentToken(): Token? = assignmentOperator

    fun getColonToken(): Token? = colonToken

    override fun getCoordinates(): Coordinates = coordinates

    override fun toString(): String {
        val colon = colonToken?.getValue() ?: ": "
        val init =
            if (initialValue != null) {
                val op = assignmentOperator?.getValue() ?: " = "
                "$op$initialValue"
            } else {
                ""
            }
        return "${getDeclarationType()} $identifier$colon$dataType$init"
    }
}
