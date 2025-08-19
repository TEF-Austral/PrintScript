package node.statement

import node.expression.Expression

class PrintStatement(private val expression: Expression) : Statement {

    fun getExpression(): Expression = expression
}