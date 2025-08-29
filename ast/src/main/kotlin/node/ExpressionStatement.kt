package node

class ExpressionStatement(
    private val expression: Expression,
) : Statement {
    fun getExpression(): Expression = expression
}
