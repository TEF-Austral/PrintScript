package executor.operators

object Equals: Operator {
    override fun canHandle(symbol: String): Boolean {
        return symbol == "=="
    }

    override fun operate(left: Any, right: Any): Any {
        return left == right
    }
}