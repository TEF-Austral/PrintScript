package executor.operators

object Sum : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == "+"

    override fun operate(
        left: String,
        right: String,
    ): OperatorResult {
        val toInt = toInt(left, right)
        if (toInt.wasSuccessful) return toInt
        val toDouble = toDouble(left, right)
        if (toDouble.wasSuccessful) return toDouble
        return OperatorResult(left + right, true)
    }

    private fun toInt(
        left: String,
        right: String,
    ): OperatorResult {
        if (left.toIntOrNull() != null && right.toIntOrNull() != null) {
            return OperatorResult(left.toInt() + right.toInt(), true)
        }
        return OperatorResult("Can't be converted to Int", false)
    }

    private fun toDouble(
        left: String,
        right: String,
    ): OperatorResult {
        if (left.toDoubleOrNull() != null && right.toDoubleOrNull() != null) {
            return OperatorResult(left.toDouble() + right.toDouble(), true)
        }
        return OperatorResult("Can't be converted to Double", false)
    }
}
