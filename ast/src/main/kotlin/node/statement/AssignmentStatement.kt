package node.statement

import Token
import node.expression.Expression

class AssignmentStatement(
    private val identifier: Token,
    private val value: Expression
) : Statement {

    fun getIdentifier(): String = identifier.getValue()
    fun getValue(): Expression = value

}