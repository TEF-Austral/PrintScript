package node

import coordinates.Coordinates
import coordinates.Position

class IfStatement(
    private val condition: Expression,
    private val consequence: Statement,
    private val alternative: Statement? = null,
    private val coordinates: Coordinates = Position(0, 0),
) : Statement {
    fun getCondition(): Expression = condition

    fun getConsequence(): Statement = consequence

    fun hasAlternative(): Boolean = alternative != null

    fun getAlternative(): Statement? = alternative

    override fun getCoordinates(): Coordinates = coordinates
}
