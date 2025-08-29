package node

class PrintStatement(
    private val expression: Expression,
) : Statement {
    fun getExpression(): Expression = expression
}
