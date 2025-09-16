// kotlin
package node

import Token
import coordinates.Coordinates

class AssignmentStatement(
    private val identifier: Token,
    private val value: Expression,
    private val coordinates: Coordinates,
    private val assignmentOperator: Token? = null, // may include original spacing (e\.g\., " = ")
) : Statement {
    fun getIdentifier(): String = identifier.getValue()

    fun getValue(): Expression = value

    fun getIdentifierToken(): Token = identifier

    fun getAssignmentToken(): Token? = assignmentOperator

    override fun getCoordinates(): Coordinates = coordinates

    override fun toString(): String {
        val op = assignmentOperator?.getValue() ?: " = "
        return "${getIdentifier()}$op${getValue()}"
    }
}
