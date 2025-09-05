package node

class IfStatement(
    private val condition: Expression,
    private val consequence: Statement,
    private val alternative: Statement? = null,
) : Statement {
    fun getCondition(): Expression = condition

    fun getConsequence(): Statement = consequence

    fun hasAlternative(): Boolean = alternative != null

    fun getAlternative(): Statement? = alternative
}
