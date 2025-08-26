package executor.operators

object LessThan: Operator {
    override fun canHandle(symbol: String): Boolean {
        return symbol == "<"
    }

    override fun operate(left: Any, right: Any): Any {
        return if (left is Int && right is Int) left < right else false
    }
}