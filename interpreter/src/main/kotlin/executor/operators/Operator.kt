package executor.operators

interface Operator {
    fun canHandle(symbol: String): Boolean

    fun operate(
        left: String,
        right: String,
    ): OperatorResult
}
