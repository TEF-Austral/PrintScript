package node.statement

import Token
import node.expression.Expression

class DeclarationStatement(
    private val identifier: Token,
    private val dataType: Token,
    private val initialValue: Expression? = null
) : Statement {

    fun getIdentifier(): String = identifier.getValue()
    fun getDataType(): String = dataType.getValue()
    fun getInitialValue(): Expression? = initialValue

}