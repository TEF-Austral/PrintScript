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
) : Statement {
    fun getDeclarationType(): CommonTypes = declarationType.getType()

    fun getIdentifier(): String = identifier.getValue()

    fun getDataType(): CommonTypes = dataType.getType()

    fun getInitialValue(): Expression? = initialValue

    fun getIdentifierToken(): Token = identifier

    fun getDataTypeToken(): Token = dataType

    fun getKeyword(): String = declarationType.getValue()

    override fun getCoordinates(): Coordinates = coordinates

    override fun toString(): String =
        "${getDeclarationType()} $identifier: $dataType${if (initialValue != null) " = $initialValue" else ""}"
}
