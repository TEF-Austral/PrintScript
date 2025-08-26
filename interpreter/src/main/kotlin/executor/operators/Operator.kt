package executor.operators

interface Operator {

    fun canHandle(symbol: String): Boolean

    fun operate(left : Any, right: Any): Any //TODO RESVISAR ESTO
}