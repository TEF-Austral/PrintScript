package executor.operators

object Multiplication : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == "*"

    override fun operate(
        left: String,
        right: String,
    ): OperatorResult {
        val toInt = toInt(left, right)
        if (toInt.wasSuccessful) return toInt
        val toDouble = toDouble(left, right)
        if (toDouble.wasSuccessful) return toDouble
        return OperatorResult("You can't multiply strings", false)
    }

    private fun toInt(
        left: String,
        right: String,
    ): OperatorResult {
        val l = left.toIntOrNull()
        val r = right.toIntOrNull()
        if (l != null && r != null) return OperatorResult(l * r, true)
        return OperatorResult("Can't be converted to Int", false)
    }

    private fun toDouble(
        left: String,
        right: String,
    ): OperatorResult {
        val l = left.toDoubleOrNull()
        val r = right.toDoubleOrNull()
        if (l != null && r != null) return OperatorResult(l * r, true)
        return OperatorResult("Can't be converted to Double", false)
    }
}
