package executor.operators

object Divide: Operator {
    override fun canHandle(symbol: String): Boolean {
        return symbol == "/"
    }

    override fun operate(left: Any, right: Any): Any {
        return if (left is Int && right is Int && right != 0) left / right else 0
    }
}