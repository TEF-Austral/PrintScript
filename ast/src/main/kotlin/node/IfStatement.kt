package node

import Token
import coordinates.Coordinates

class IfStatement(
    private val condition: Expression,
    private val consequence: Statement,
    private val alternative: Statement? = null,
    private val coordinates: Coordinates,
    // optional raw tokens/metadata to preserve original layout when config is null
    private val ifHeaderToken: Token? = null, // e.g., "if (" or "if("
    private val closeParenToken: Token? = null, // e.g., ") " or ")"
    private val elseTokenRaw: Token? = null, // e.g., " else " or "\nelse "
    private val bracesOnSameLine: Boolean? = null, // true if original had ") {"
) : Statement {
    fun getCondition(): Expression = condition

    fun getConsequence(): Statement = consequence

    fun hasAlternative(): Boolean = alternative != null

    fun getAlternative(): Statement? = alternative

    fun getIfHeaderToken(): Token? = ifHeaderToken

    fun getCloseParenToken(): Token? = closeParenToken

    fun getElseTokenRaw(): Token? = elseTokenRaw

    fun isBracesOnSameLine(): Boolean? = bracesOnSameLine

    override fun getCoordinates(): Coordinates = coordinates
}
