package executor.operators

object Divide : Operator {
    override fun canHandle(symbol: String): Boolean = symbol == "/"

    override fun operate(
        left: String,
        right: String,
    ): OperatorResult {
        val toInt = toInt(left, right)
        if (toInt.wasSuccessful) return toInt
        val toDouble = toDouble(left, right)
        if (toDouble.wasSuccessful) return toDouble
        return OperatorResult("You can't divide strings", false)
    }

    private fun toInt(
        left: String,
        right: String,
    ): OperatorResult {
        val l = left.toIntOrNull()
        val r = right.toIntOrNull()
        if (l != null && r != null) {
            if (r == 0) return OperatorResult("Division by zero", false)
            if (l % r != 0) return OperatorResult(l.toDouble() / r, true)
            return OperatorResult(l / r, true)
        }
        return OperatorResult("Can't be converted to Int", false)
    }

    private fun toDouble(
        left: String,
        right: String,
    ): OperatorResult {
        val l = left.toDoubleOrNull()
        val r = right.toDoubleOrNull()
        if (l != null && r != null) {
            if (r == 0.0) return OperatorResult("Division by zero", false)
            return OperatorResult(l / r, true)
        }
        return OperatorResult("Can't be converted to Double", false)
    }
}
