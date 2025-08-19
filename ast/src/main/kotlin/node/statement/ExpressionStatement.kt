package node.statement

import node.expression.Expression

class ExpressionStatement(private val expression: Expression) : Statement {

    fun getExpression(): Expression = expression

}