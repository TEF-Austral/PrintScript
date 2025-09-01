package node

import Token
import type.CommonTypes

class DeclarationStatement(
    private val identifier: Token,
    private val dataType: Token,
    private val initialValue: Expression? = null,
) : Statement {
    fun getIdentifier(): String = identifier.getValue()

    fun getDataType(): CommonTypes = dataType.getType()

    fun getInitialValue(): Expression? = initialValue
}
