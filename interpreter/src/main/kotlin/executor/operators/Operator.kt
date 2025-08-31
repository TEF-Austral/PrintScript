package executor.operators

import variable.Variable

interface Operator {
    fun canHandle(symbol: String): Boolean

    fun operate(
        left: Variable,
        right: Variable,
    ): Variable
}
